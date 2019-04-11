package ssjava.cholmod.demo;

/**
 * This file is more-or-less directly copied from the original C source file, cholmod_demo.c.
 * The project-wide MIT license does not apply to this file.  The original copyright  and headers are
 * reproduced below.
 *
 * The intent of this copy of cholmod_demo is to provide proof-of-concept and higher-level
 * testing.  It is not to be used as an example of best programming practices utilizing these
 * wrappers.
 */

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.byref.PointerByReference;
import ssjava.cholmod.*;
import ssjava.suitesparse_config.JnrLibcExtra;
import ssjava.suitesparse_config.SuiteSparse_config;

import java.io.*;

import static ssjava.cholmod.CholmodStatus.CHOLMOD_INVALID;
import static ssjava.cholmod.SolveSystem.CHOLMOD_A;
import static ssjava.cholmod.XType.CHOLMOD_PATTERN;
import static ssjava.cholmod.XType.CHOLMOD_REAL;
import static ssjava.cholmod.cholmod_common.CHOLMOD_MAXMETHODS;

public class CholmodDemo
{
    /* ========================================================================== */
    /* === Demo/cholmod_demo ==================================================== */
    /* ========================================================================== */

    /* -----------------------------------------------------------------------------
     * CHOLMOD/Demo Module.  Copyright (C) 2005-2013, Timothy A. Davis
     * -------------------------------------------------------------------------- */

    /* Read in a matrix from a file, and use CHOLMOD to solve Ax=b if A is
     * symmetric, or (AA'+beta*I)x=b otherwise.  The file format is a simple
     * triplet format, compatible with most files in the Matrix Market format.
     * See cholmod_read.c for more details.  The readhb.f program reads a
     * Harwell/Boeing matrix (excluding element-types) and converts it into the
     * form needed by this program.  reade.f reads a matrix in Harwell/Boeing
     * finite-element form.
     *
     * Usage:
     *	cholmod_demo matrixfile
     *	cholmod_demo < matrixfile
     *
     * The matrix is assumed to be positive definite (a supernodal LL' or simplicial
     * LDL' factorization is used).
     *
     * Requires the Core, Cholesky, MatrixOps, and Check Modules.
     * Optionally uses the Partition and Supernodal Modules.
     * Does not use the Modify Module.
     *
     * See cholmod_simple.c for a simpler demo program.
     *
     * cholmod_demo is the same as cholmod_l_demo, except for the size of the
     * basic integer (int vs SuiteSparse_long)
     */
    public static int NTRIALS = 100;

    private static final JnrLibcExtra jle = JnrLibcExtra.Load();
    private static final Core core = Core.Load();
    private static final Cholesky cholesky = Cholesky.Load();
    private static final SuiteSparse_config sscfg = SuiteSparse_config.Load();
    private static final Check check = Check.Load();
    private static final MatrixOps mops = MatrixOps.Load();

    /* ff is a global variable so that it can be closed by my_handler */
    private Pointer ff = null;

    /* halt if an error occurs */
    private ErrorHandler my_handler = (status, file, line, message) ->
    {
        System.err.format("cholmod error: file: %s line: %d status: %s: %s\n",
                file, line, status, message) ;
        if (status.intValue() < 0)
        {
            if (ff != null) jle.fclose(ff);
            throw new RuntimeException("Received Error Status");
        }
    };

    public static void main(String...args) throws  Exception
    {
        new CholmodDemo().rundemo(System.out, args);
    }

    public void rundemo ( OutputStream out, String...args) throws IOException
    {
        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
        Runtime runtime = Runtime.getSystemRuntime();
        double[] resid = new double[4];
        double[] ts = new double[3];

        double t, ta, tf, tot, bnorm, xnorm, anorm, rnorm, fl,
            anz, axbnorm=0.0, rnorm2, resid2, rcond ;
        Pointer f;
        cholmod_sparse A ;
        cholmod_dense X = null, B, W, R;
        double[] one = new double[2], zero =new double[2], minusone =new double[2], beta =new double[2] ;
        double xlnz;
        cholmod_common cm ;
        cholmod_factor L ;
        double[] Bx;
        double[] Rx = null;
        double[] Xx ;
        int i;
        int n;
        int isize;
        int xsize;
        Order ordering;
        XType xtype;
        int s;
        int ss;
        int lnz ;
        int trial, method, L_is_super ;
        int[] ver = new int[3] ;

        ts[0] = 0.;
        ts[1] = 0.;
        ts[2] = 0.;

        /* ---------------------------------------------------------------------- */
        /* get the file containing the input matrix */
        /* ---------------------------------------------------------------------- */

        ff = null;
        int argc = args.length;
        if (argc > 0)
        {
            if ((f = jle.fopen (args [0], "r")) == null)
            {
                my_handler.report (CHOLMOD_INVALID, "", -1,
                        "unable to open file");
            }
            ff = f ;
        }
        else
        {
            f = JnrLibcExtra.StdioStream(jle.stdin());
        }

        /* ---------------------------------------------------------------------- */
        /* start CHOLMOD and set parameters */
        /* ---------------------------------------------------------------------- */

        cm = new cholmod_common(runtime);
        core.cholmod_start (cm) ;
        R = core.cholmod_allocate_dense(0,0,1, CHOLMOD_REAL,cm);
//        CHOLMOD_FUNCTION_DEFAULTS ;     /* just for testing (not required) */

        /* use default parameter settings, except for the error handler.  This
         * demo program terminates if an error occurs (out of memory, not positive
         * definite, ...).  It makes the demo program simpler (no need to check
         * CHOLMOD error conditions).  This non-default parameter setting has no
         * effect on performance. */
        cm.error_handler.set(my_handler);

        /* Note that CHOLMOD will do a supernodal LL' or a simplicial LDL' by
         * default, automatically selecting the latter if flop/nnz(L) < 40. */

        /* ---------------------------------------------------------------------- */
        /* create basic scalars */
        /* ---------------------------------------------------------------------- */

        zero [0] = 0 ;
        zero [1] = 0 ;
        one [0] = 1 ;
        one [1] = 0 ;
        minusone [0] = -1 ;
        minusone [1] = 0 ;
        beta [0] = 1e-6 ;
        beta [1] = 0 ;

        /* ---------------------------------------------------------------------- */
        /* read in a matrix */
        /* ---------------------------------------------------------------------- */

        pw.format("\n---------------------------------- cholmod_demo:\n") ;
        core.cholmod_version (ver) ;
        pw.format ("cholmod version %d.%d.%d\n", ver [0], ver [1], ver [2]) ;
        sscfg.SuiteSparse_version (ver) ;
        pw.format("SuiteSparse version %d.%d.%d\n", ver [0], ver [1], ver [2]) ;
        A = check.cholmod_read_sparse (f, cm) ;
        if (ff != null)
        {
            jle.fclose (ff) ;
            ff = null;
        }
        xtype = A.xtype.get();
        anorm = 1 ;
        anorm = mops.cholmod_norm_sparse (A, 0, cm) ;
        pw.format("norm (A,inf) = %-10.0f\n", anorm) ;
        pw.format("norm (A,1)   = %-10.0f\n", mops.cholmod_norm_sparse (A, 1, cm)) ;
        pw.flush();
        check.cholmod_print_sparse (A, "A", cm) ;
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));

        if (A.nrow.intValue() > A.ncol.intValue())
        {
            /* Transpose A so that A'A+beta*I will be factorized instead */
            cholmod_sparse C = core.cholmod_transpose (A, 2, cm) ;
            Core.Cholmod_Free_Sparse(core,A,cm);
            A = C ;
            pw.println("transposing input matrix") ;
        }

        /* ---------------------------------------------------------------------- */
        /* create an arbitrary right-hand-side */
        /* ---------------------------------------------------------------------- */

        n = A.nrow.intValue() ;
        B = core.cholmod_zeros (n, 1, xtype, cm) ;
        Bx = B.getDoubleValues().getX();

/*#if GHS
        {
            / * b = A*ones(n,1), used by Gould, Hu, and Scott in their experiments * /
            cholmod_dense *X0 ;
            X0 = cholmod_ones (A->ncol, 1, xtype, cm) ;
            cholmod_sdmult (A, 0, one, zero, X0, B, cm) ;
            cholmod_free_dense (&X0, cm) ;
        }
#else */
        if (xtype == CHOLMOD_REAL)
        {
            /* real case */
            for (i = 0 ; i < n ; i++)
            {
                double x = n ;
                Bx [i] = 1 + i / x ;
            }
        }
        else
        {
            /* complex case */
            for (i = 0 ; i < n ; i++)
            {
                double x = n ;
                Bx [2*i  ] = 1 + i / x ;		/* real part of B(i) */
                Bx [2*i+1] = (x/2 - i) / (3*x) ;	/* imag part of B(i) */
            }
        }
        B.setValues(Bx, null);
//#endif

        pw.flush();
        check.cholmod_print_dense (B, "B", cm) ;
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));
        bnorm = mops.cholmod_norm_dense (B, 0, cm) ;	/* max norm */
        pw.format("bnorm %g\n", bnorm) ;

            /* ---------------------------------------------------------------------- */
            /* analyze and factorize */
            /* ---------------------------------------------------------------------- */

        t = sscfg.SuiteSparse_time();
        L = cholesky.cholmod_analyze (A, cm) ;
        ta = sscfg.SuiteSparse_time() - t ;
        ta = Math.max (ta, 0) ;

        pw.format("Analyze: flop %g lnz %g\n", cm.fl.doubleValue(), cm.lnz.doubleValue()) ;

        if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            pw.println("Factorizing A*A'+beta*I") ;
            t = sscfg.SuiteSparse_time();
            cholesky.cholmod_factorize_p (A, beta, null, 0, L, cm) ;
            tf = sscfg.SuiteSparse_time() - t ;
            tf = Math.max (tf, 0) ;
        }
        else
        {
            pw.println("Factorizing A") ;
            t = sscfg.SuiteSparse_time() ;
            cholesky.cholmod_factorize (A, L, cm) ;
            tf = sscfg.SuiteSparse_time() - t ;
            tf = Math.max (tf, 0) ;
        }

        pw.flush();
        check.cholmod_print_factor (L, "L", cm) ;
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));

        /* determine the # of integers's and reals's in L.  See cholmod_free */
        if (L.is_super.intValue() != 0)
        {
            s = L.nsuper.intValue() + 1 ;
            xsize = L.xsize.intValue() ;
            ss = L.ssize.intValue() ;
            isize =
                    n	/* L->Perm */
                    + n	/* L->ColCount, nz in each column of 'pure' L */
                    + s	/* L->pi, column pointers for L->s */
                    + s	/* L->px, column pointers for L->x */
                    + s	/* L->super, starting column index of each supernode */
                    + ss ;	/* L->s, the pattern of the supernodes */
        }
        else
        {
            /* this space can increase if you change parameters to their non-
             * default values (cm->final_pack, for example). */
            lnz = L.nzmax.intValue() ;
            xsize = lnz ;
            isize =
                    n	/* L->Perm */
                    + n	/* L->ColCount, nz in each column of 'pure' L */
                    + n+1	/* L->p, column pointers */
                    + lnz	/* L->i, integer row indices */
                    + n	/* L->nz, nz in each column of L */
                    + n+2	/* L->next, link list */
                    + n+2 ;	/* L->prev, link list */
        }

        /* solve with Bset will change L from simplicial to supernodal */
        rcond = cholesky.cholmod_rcond (L, cm) ;
        L_is_super = L.is_super.intValue() ;

        /* ---------------------------------------------------------------------- */
        /* solve */
        /* ---------------------------------------------------------------------- */

        for (method = 0 ; method <= 3 ; method++)
        {
            double x = n ;
            resid [method] = -1 ;       /* not yet computed */

            if (method == 0)
            {
                /* basic solve, just once */
                t = sscfg.SuiteSparse_time() ;
                X = cholesky.cholmod_solve (CHOLMOD_A, L, B, cm) ;
                ts [0] = sscfg.SuiteSparse_time() - t ;
                ts [0] = Math.max (ts [0], 0) ;
            }
            else if (method == 1)
            {
                /* basic solve, many times, but keep the last one */
                t = sscfg.SuiteSparse_time() ;
                for (trial = 0 ; trial < NTRIALS ; trial++)
                {
                    Core.Cholmod_Free_Dense(core,X,cm); X = null;
                    Bx [0] = 1 + trial / x ;        /* tweak B each iteration */
                    B.x.get().putDouble(0, Bx[0]);
                    X = cholesky.cholmod_solve (CHOLMOD_A, L, B, cm) ;
                }
                ts [1] = sscfg.SuiteSparse_time() - t ;
                ts [1] = Math.max (ts [1], 0) / NTRIALS ;
            }
            else if (method == 2)
            {
                /* solve with reused workspace */
                PointerByReference Ywork = new PointerByReference(null);
                PointerByReference Ework = new PointerByReference(null);
                Core.Cholmod_Free_Dense(core, X, cm) ;
                PointerByReference Xptr = new PointerByReference();

                t = sscfg.SuiteSparse_time() ;
                for (trial = 0 ; trial < NTRIALS ; trial++)
                {
                    Bx [0] = 1 + trial / x ;        /* tweak B each iteration */
                    cholesky.cholmod_solve2 (CHOLMOD_A, L, B, null, Xptr, null,
                    Ywork, Ework, cm) ;
                }
                X.useMemory(Xptr.getValue());
                core.cholmod_free_dense (Ywork, cm) ;
                core.cholmod_free_dense (Ework, cm) ;
                ts [2] = sscfg.SuiteSparse_time() - t ;
                ts [2] = Math.max (ts [2], 0) / NTRIALS ;
            }
            else
            {
                /* solve with reused workspace and sparse Bset */
                PointerByReference Ywork = new PointerByReference();
                PointerByReference Ework = new PointerByReference();
                PointerByReference ptrX = new PointerByReference(Struct.getMemory(X));
                PointerByReference ptrX2 = new PointerByReference();
                PointerByReference ptrXset = new PointerByReference();
                cholmod_sparse Xset = new cholmod_sparse(runtime);
                cholmod_dense X2 = new cholmod_dense(runtime);

                int xlen, j;
                int[] Xseti, Lnz;
                double err;
                double[] X1x, X2x;
                PrintWriter timelog = new PrintWriter(new FileWriter("timelog.m")) ;
                if (timelog != null) timelog.println("results = [") ;

                cholmod_dense B2 = core.cholmod_zeros (n, 1, xtype, cm) ;

                double[] B2x = B2.getDoubleValues().getX();

                cholmod_sparse Bset = core.cholmod_allocate_sparse(n, 1, 1, 0, 1,
                        cholmod_sparse.SType.UNSYMMETRIC, CHOLMOD_PATTERN, cm);
                Bset.p.get().put(0,new int[] {0,1},0,2);
                Pointer Bseti = Bset.i.get() ;
                resid [3] = 0 ;

                for (i = 0 ; i < Math.min (100,n) ; i++)
                {
                /* B (i) is nonzero, all other entries are ignored
                   (implied to be zero) */
                    Bseti.putInt(0, i) ;
                    if (xtype == CHOLMOD_REAL)
                    {
                        B2x [i] = 3.1 * i + 0.9 ;
                    }
                    else
                    {
                        B2x [2*i  ] = i + 0.042 ;
                        B2x [2*i+1] = i - 92.7 ;
                    }

                    /* first get the entire solution, to compare against */
                    B2.setValues(B2x,null);
                    cholesky.cholmod_solve2 (CHOLMOD_A, L, B2, null, ptrX, null,
                        Ywork, Ework, cm) ;
                    X.useMemory(ptrX.getValue());

                /* now get the sparse solutions; this will change L from
                   supernodal to simplicial */

                    if (i == 0)
                    {
                    /* first solve can be slower because it has to allocate
                       space for X2, Xset, etc, and change L.
                       So don't time it */
                        cholesky.cholmod_solve2 (CHOLMOD_A, L, B2, Bset, ptrX2, ptrXset,
                        Ywork, Ework, cm) ;
                    }

                    t = sscfg.SuiteSparse_time();
                    for (trial = 0 ; trial < NTRIALS ; trial++)
                    {
                    /* solve Ax=b but only to get x(i).
                       b is all zero except for b(i).
                       This takes O(xlen) time */
                        cholesky.cholmod_solve2 (CHOLMOD_A, L, B2, Bset, ptrX2, ptrXset,
                        Ywork, Ework, cm) ;
                    }
                    t = sscfg.SuiteSparse_time() - t ;
                    t = Math.max (t, 0) / NTRIALS ;

                    Xset.useMemory(ptrXset.getValue());
                    X2.useMemory(ptrX2.getValue());
                    /* check the solution and log the time */
                    Xseti = new int[Xset.nzmax.intValue()];
                    Xset.i.get().get(0, Xseti, 0, Xseti.length);
                    xlen = Xset.p.get().getInt(Integer.BYTES /* offset 1 */);
                    X1x = X.getDoubleValues().getX();
                    X2x = X2.getDoubleValues().getX();
                    Lnz = new int[A.ncol.intValue()];
                    L.nz.get().get(0, Lnz, 0, Lnz.length);

                /*
                printf ("\ni %d xlen %d  (%p %p)\n", i, xlen, X1x, X2x) ;
                */

                    if (xtype == CHOLMOD_REAL)
                    {
                        fl = 2 * xlen ;
                        for (int k = 0 ; k < xlen ; k++)
                        {
                            j = Xseti [k] ;
                            fl += 4 * Lnz [j] ;
                            err = X1x [j] - X2x [j] ;
                            err = Math.abs(err) ;
                            resid [3] = Math.max (resid [3], err) ;
                        }
                    }
                    else
                    {
                        fl = 16 * xlen ;
                        for (int k = 0 ; k < xlen ; k++)
                        {
                            j = Xseti [k] ;
                            fl += 16 * Lnz [j] ;
                            err = X1x [2*j  ] - X2x [2*j  ] ;
                            err = Math.abs(err) ;
                            resid [3] = Math.max (resid [3], err) ;
                            err = X1x [2*j+1] - X2x [2*j+1] ;
                            err = Math.abs(err) ;
                            resid [3] = Math.max(resid [3], err) ;
                        }
                    }
                    if (timelog != null) timelog.format("%g %g %g %g\n",
                            (double) i, (double) xlen, fl, t);

                    /* clear B for the next test */
                    if (xtype == CHOLMOD_REAL)
                    {
                        B2x [i] = 0 ;
                    }
                    else
                    {
                        B2x [2*i  ] = 0 ;
                        B2x [2*i+1] = 0 ;
                    }
                }


                if (timelog != null)
                {
                    timelog.format("] ; resid = %g ;\n", resid [3]) ;
                    timelog.format("lnz = %g ;\n", cm.lnz.doubleValue()) ;
                    timelog.format("t = %g ;   %% dense solve time\n", ts [2]) ;
                    timelog.close();
                    timelog = null;
                }

// #ifndef NMATRIXOPS
                resid [3] = resid [3] / mops.cholmod_norm_dense (X, 1, cm) ;
//#endif

                core.cholmod_free_dense (Ywork, cm) ;
                core.cholmod_free_dense (Ework, cm) ;
                core.cholmod_free_dense (ptrX2, cm) ;
                Core.Cholmod_Free_Dense(core,B2,cm);
                core.cholmod_free_sparse (ptrXset, cm) ;
                Core.Cholmod_Free_Sparse(core, Bset, cm);
            }

            /* ------------------------------------------------------------------ */
            /* compute the residual */
            /* ------------------------------------------------------------------ */

            if (method < 3)
            {
// #ifndef NMATRIXOPS

                if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
                {
                    /* (AA'+beta*I)x=b is the linear system that was solved */
                    /* W = A'*X */
                    W = core.cholmod_allocate_dense (A.ncol.intValue(), 1, A.ncol.intValue(), xtype, cm) ;
                    mops.cholmod_sdmult (A, 2, one, zero, X, W, cm) ;
                    /* R = B - beta*X */
                    Core.Cholmod_Free_Dense(core,R,cm);
                    R = core.cholmod_zeros (n, 1, xtype, cm) ;
                    Rx = R.getDoubleValues().getX();
                    Xx = X.getDoubleValues().getX();
                    if (xtype == CHOLMOD_REAL)
                    {
                        for (i = 0 ; i < n ; i++)
                        {
                            Rx [i] = Bx [i] - beta [0] * Xx [i] ;
                        }
                    }
                    else
                    {
                        /* complex case */
                        for (i = 0 ; i < n ; i++)
                        {
                            Rx [2*i  ] = Bx [2*i  ] - beta [0] * Xx [2*i  ] ;
                            Rx [2*i+1] = Bx [2*i+1] - beta [0] * Xx [2*i+1] ;
                        }
                    }
                    /* R = A*W - R */
                    R.setValues(Rx,null);
                    mops.cholmod_sdmult (A, 0, one, minusone, W, R, cm) ;
                    Rx = R.getDoubleValues().getX();
                    Core.Cholmod_Free_Dense(core, W, cm);
                }
                else
                {
                    /* Ax=b was factorized and solved, R = B-A*X */
                    Core.Cholmod_Free_Dense(core, R, cm);
                    R = core.cholmod_copy_dense (B, cm) ;
                    mops.cholmod_sdmult (A, 0, minusone, one, X, R, cm) ;
                }
                rnorm = -1 ;
                xnorm = 1 ;
                rnorm = mops.cholmod_norm_dense (R, 0, cm) ;	    /* max abs. entry */
                xnorm = mops.cholmod_norm_dense (X, 0, cm) ;	    /* max abs. entry */
                axbnorm = (anorm * xnorm + bnorm + ((n == 0) ? 1 : 0)) ;
                resid [method] = rnorm / axbnorm ;
/*#else
                printf ("residual not computed (requires CHOLMOD/MatrixOps)\n") ;
#endif */
            }
        }

        tot = ta + tf + ts [0] ;

        /* ---------------------------------------------------------------------- */
        /* iterative refinement (real symmetric case only) */
        /* ---------------------------------------------------------------------- */

        resid2 = -1 ;
//#ifndef NMATRIXOPS
        if (A.stype.get() != cholmod_sparse.SType.UNSYMMETRIC && A.xtype.get() == CHOLMOD_REAL)
        {
            /* R2 = A\(B-A*X) */
            cholmod_dense R2 = cholesky.cholmod_solve (CHOLMOD_A, L, R, cm) ;
            /* compute X = X + A\(B-A*X) */
            Xx = X.getDoubleValues().getX();
            Rx = R.getDoubleValues().getX();
            for (i = 0 ; i < n ; i++)
            {
                Xx [i] = Xx [i] + Rx [i] ;
            }
            X.setValues(Xx, null);
            Core.Cholmod_Free_Dense(core, R2, cm);
            Core.Cholmod_Free_Dense(core, R, cm);

            /* compute the new residual, R = B-A*X */
            R = core.cholmod_copy_dense (B, cm);
            mops.cholmod_sdmult (A, 0, minusone, one, X, R, cm) ;
            rnorm2 = mops.cholmod_norm_dense (R, 0, cm) ;
            resid2 = rnorm2 / axbnorm ;
        }
//#endif

        Core.Cholmod_Free_Dense(core, R, cm);

        /* ---------------------------------------------------------------------- */
        /* print results */
        /* ---------------------------------------------------------------------- */

        anz = cm.anz.doubleValue() ;
        for (i = 0 ; i < CHOLMOD_MAXMETHODS ; i++)
        {
            fl = cm.method[i].fl.doubleValue() ;
            xlnz = cm.method[i].lnz.doubleValue() ;
            cm.method [i].fl.set(-1) ;
            cm.method [i].lnz.set(-1);
            ordering = cm.method[i].ordering.get() ;
            if (fl >= 0)
            {
                pw.print("Ordering: ") ;
                if (ordering == Order.CHOLMOD_POSTORDERED) pw.print("postordered ") ;
                if (ordering == Order.CHOLMOD_NATURAL)     pw.print("natural ") ;
                if (ordering == Order.CHOLMOD_GIVEN)	   pw.print("user    ") ;
                if (ordering == Order.CHOLMOD_AMD)	       pw.print("AMD     ") ;
                if (ordering == Order.CHOLMOD_METIS)	   pw.print("METIS   ") ;
                if (ordering == Order.CHOLMOD_NESDIS)      pw.print("NESDIS  ") ;
                if (xlnz > 0)
                {
                    pw.format("fl/lnz %10.1f", fl / xlnz) ;
                }
                if (anz > 0)
                {
                    pw.format("  lnz/anz %10.1f", xlnz / anz) ;
                }
                pw.println();
            }
        }

        pw.format("ints in L: %15.0f, doubles in L: %15.0f\n",
                (double) isize, (double) xsize) ;
        pw.format("factor flops %g nnz(L) %15.0f (w/no amalgamation)\n",
                cm.fl.doubleValue(), cm.lnz.doubleValue()) ;
        if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            pw.format("nnz(A):    %15.0f\n", cm.anz.doubleValue()) ;
        }
        else
        {
            pw.format("nnz(A*A'): %15.0f\n", cm.anz.doubleValue()) ;
        }
        if (cm.lnz.doubleValue() > 0)
        {
            pw.format("flops / nnz(L):  %8.1f\n", cm.fl.doubleValue() / cm.lnz.doubleValue()) ;
        }
        if (anz > 0)
        {
            pw.format("nnz(L) / nnz(A): %8.1f\n", cm.lnz.doubleValue() / cm.anz.doubleValue()) ;
        }
        pw.format("analyze cputime:  %12.4f\n", ta) ;
        pw.format("factor  cputime:   %12.4f mflop: %8.1f\n", tf,
                (tf == 0) ? 0 : (1e-6*cm.fl.doubleValue() / tf)) ;
        pw.format("solve   cputime:   %12.4f mflop: %8.1f\n", ts [0],
                (ts [0] == 0) ? 0 : (1e-6*4*cm.lnz.doubleValue() / ts [0])) ;
        pw.format("overall cputime:   %12.4f mflop: %8.1f\n",
                tot, (tot == 0) ? 0 : (1e-6 * (cm.fl.doubleValue() + 4 * cm.lnz.doubleValue()) / tot)) ;
        pw.format("solve   cputime:   %12.4f mflop: %8.1f (%d trials)\n", ts [1],
                (ts [1] == 0) ? 0 : (1e-6*4*cm.lnz.doubleValue() / ts [1]), NTRIALS) ;
        pw.format("solve2  cputime:   %12.4f mflop: %8.1f (%d trials)\n", ts [2],
                (ts [2] == 0) ? 0 : (1e-6*4*cm.lnz.doubleValue() / ts [2]), NTRIALS) ;
        pw.format("peak memory usage: %12.0f (MB)\n",
                (double) (cm.memory_usage.longValue()) / 1048576.) ;
        pw.print("residual (|Ax-b|/(|A||x|+|b|)): ") ;
        for (method = 0 ; method <= 3 ; method++)
        {
            pw.format("%8.2e ", resid [method]) ;
        }
        pw.println();
        if (resid2 >= 0)
        {
            pw.format("residual %8.1e (|Ax-b|/(|A||x|+|b|))", resid2);
            pw.println(" after iterative refinement") ;
        }

        pw.format("rcond    %8.1e\n\n", rcond) ;

        if (L_is_super != 0)
        {
            check.cholmod_gpu_stats (cm) ;
        }

        Core.Cholmod_Free_Factor(core,L,cm);
        Core.Cholmod_Free_Dense(core,X,cm);

        /* ---------------------------------------------------------------------- */
        /* free matrices and finish CHOLMOD */
        /* ---------------------------------------------------------------------- */

        Core.Cholmod_Free_Sparse(core, A, cm);
        Core.Cholmod_Free_Dense(core,B,cm);
        core.cholmod_finish (cm) ;
        pw.flush();

    }

}
