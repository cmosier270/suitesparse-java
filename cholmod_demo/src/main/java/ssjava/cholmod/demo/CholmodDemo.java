/**
 * The suitesparse-java MIT licence does not apply to this file.
 *
 * This source code was translated as directly as possible from the original C
 * Code within the SuiteSparse package, with some reorganization to make the large
 * main method hopefully more manageable. The copyright notice from original
 * code has been replicated below.
 */
/* ========================================================================== */
/* === Demo/cholmod_demo ==================================================== */
/* ========================================================================== */

/* -----------------------------------------------------------------------------
 * CHOLMOD/Demo Module.  Copyright (C) 2005-2013, Timothy A. Davis
 * -------------------------------------------------------------------------- */


package ssjava.cholmod.demo;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.byref.PointerByReference;
import ssjava.cholmod.*;
import ssjava.suitesparse_config.JnrLibcExtra;
import ssjava.suitesparse_config.SuiteSparseVersion;
import ssjava.suitesparse_config.SuiteSparse_config;

import java.io.*;

import static ssjava.cholmod.SolveSystem.CHOLMOD_A;
import static ssjava.cholmod.XType.*;
import static ssjava.cholmod.cholmod_common.CHOLMOD_MAXMETHODS;

public class CholmodDemo
{
    private final static int NTRIALS = 100;
    private final cholmod_common cm;
    private final Core core;
    private final Check check = Check.Load();
    private final MatrixOps mops = MatrixOps.Load();
    private final SuiteSparse_config cfg = SuiteSparse_config.Load();
    private final Cholesky chol = Cholesky.Load();
    private final JnrLibcExtra jle = JnrLibcExtra.Load();
    private final Pointer stdout = JnrLibcExtra.StdioStream(jle.stdout());
    private final PrintWriter pw;
    public static final double[] zero = {0.0, 0.0};
    public static final double[] one = {1.0, 0.0};
    public static final double[] minusone = {-1.0, 0.0};
    public static final double[] beta = {1e-6, 0.0};
    private double anorm = 1.0;
    private cholmod_sparse A;
    private cholmod_dense B;
    private double[] Bx;
    private int nrow, ncol;
    private XType aXtype;
    private double bnorm;

    private class FactorParms
    {
        final int isize;
        final int xsize;
        final cholmod_factor L;

        public FactorParms(cholmod_factor L)
        {
            this.L = L;
            /* determine the # of integers's and reals's in L.  See cholmod_free */
            if (L.is_super.intValue() != 0)
            {
                int s = L.nsuper.intValue() + 1;
                xsize = L.xsize.intValue();
                int ss = L.ssize.intValue();
                isize = nrow    /* L->Perm */
                        + nrow    /* L->ColCount, nz in each column of 'pure' L */
                        + s    /* L->pi, column pointers for L->s */
                        + s    /* L->px, column pointers for L->x */
                        + s    /* L->super, starting column index of each supernode */
                        + ss;    /* L->s, the pattern of the supernodes */
            }
            else
            {
                /* this space can increase if you change parameters to their non-
                 * default values (cm->final_pack, for example). */
                int lnz = L.nzmax.intValue();
                ;
                xsize = lnz;
                isize = nrow    /* L->Perm */
                        + nrow    /* L->ColCount, nz in each column of 'pure' L */
                        + nrow + 1    /* L->p, column pointers */
                        + lnz    /* L->i, integer row indices */
                        + nrow    /* L->nz, nz in each column of L */
                        + nrow + 2    /* L->next, link list */
                        + nrow + 2;    /* L->prev, link list */
            }
        }

        public int getIsize()
        {
            return isize;
        }

        public int getXsize()
        {
            return xsize;
        }
        public cholmod_factor getL() {return L;}
    }

    private class Residual
    {
        final cholmod_dense R;
        final double axbnorm;
        final double resid;

        private Residual(cholmod_dense r, double axbnorm, double resid)
        {
            R = r;
            this.axbnorm = axbnorm;
            this.resid = resid;
        }

        public cholmod_dense getR()
        {
            return R;
        }

        public double getAxbnorm()
        {
            return axbnorm;
        }

        public double getResid()
        {
            return resid;
        }

        public void cleanup()
        {
            Core.Cholmod_Free_Dense(core, R, cm);
        }
    }

    private class TimeRecord
    {
        private double analyze;
        private double factorize;
        private double solveMethod0;
        private double solveMethod1;
        private double solveMethod2;
        private double solveMethod3;

        public double getAnalyze()
        {
            return analyze;
        }

        public void setAnalyze(double timeAnalyze)
        {
            analyze = Math.max(0, cfg.SuiteSparse_time() - timeAnalyze);
        }

        public double getFactorize()
        {
            return factorize;
        }

        public void setFactorize(double v)
        {
            factorize = Math.max(0, cfg.SuiteSparse_time() - v);
        }

        public double getSolveMethod0()
        {
            return solveMethod0;
        }

        public void setSolveMethod0(double solveMethod0)
        {
            this.solveMethod0 = timeTrials(solveMethod0);
        }

        public double getSolveMethod1()
        {
            return solveMethod1;
        }

        public void setSolveMethod1(double solveMethod1)
        {
            this.solveMethod1 = timeTrials(solveMethod1);
        }

        public double getSolveMethod2()
        {
            return solveMethod2;
        }

        public void setSolveMethod2(double solveMethod2)
        {
            this.solveMethod2 = timeTrials(solveMethod2);
        }

        public double getSolveMethod3()
        {
            return solveMethod3;
        }

        public void setSolveMethod3(double solveMethod3)
        {
            this.solveMethod3 = timeTrials(solveMethod3);
        }

        private double timeTrials(double solve)
        {
            return Math.max(0, cfg.SuiteSparse_time() - solve) / NTRIALS;
        }
    }

    private final TimeRecord timeRecord = new TimeRecord();

    public CholmodDemo(cholmod_common cm, Core core, PrintWriter pw)
    {
        this.cm = cm;
        this.core = core;
        this.pw = pw;
    }


    public void runDemo(cholmod_sparse A) throws IOException
    {
        this.A = A;
        aXtype = A.xtype.get();
        prep();
        createRightHandSide();
        FactorParms factorParms = analyzeFactorize();
        cholmod_factor L = factorParms.getL();
        double[] resid = new double[5];
        double rcond = chol.cholmod_rcond(L, cm);;
        int L_is_super = L.is_super.intValue();
        cholmod_dense X = solve(B, L, resid);
        printResults(X, resid, rcond, L_is_super, factorParms);
        Core.Cholmod_Free_Dense(core,B,cm);
        Core.Cholmod_Free_Dense(core,X,cm);
        Core.Cholmod_Free_Factor(core,L,cm);
    }


    private void printResults(cholmod_dense X, double[] resid, double rcond, int l_is_super, FactorParms factorParms)
    {
        pw.flush();
        double anz = cm.anz.doubleValue();
        for (int i = 0; i < CHOLMOD_MAXMETHODS; ++i)
        {
            CholmodMethod mthd = cm.method[i];
            double fl = mthd.fl.doubleValue();
            double xlnz = mthd.lnz.doubleValue();
            mthd.fl.set(-1.0);
            mthd.lnz.set(-1.0);
            Order ordering = mthd.ordering.get();
            if (fl >= 0)
            {
                pw.format("Ordering: %s ", ordering.toString());
                if (xlnz > 0)
                {
                    pw.format("fl/lnz %10.1f", fl / xlnz);
                }
                if (anz > 0)
                {
                    pw.format("  lnz/anz %10.1f", xlnz / anz);
                }
                pw.println();
            }
        }
        pw.format("ints in L: %15.0f, doubles in L: %15.0f\n",
                (double) factorParms.getIsize(), (double) factorParms.getXsize());
        double cmlnz = cm.lnz.doubleValue();
        double cmfl = cm.fl.doubleValue();
        pw.format("factor flops %g nnz(L) %15.0f (w/no amalgamation)\n",
                cmfl, cmlnz);
        if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            pw.format("nnz(A):    %15.0f\n", anz) ;
        }
        else
        {
            pw.format("nnz(A*A'): %15.0f\n", anz) ;
        }
        if (cmlnz > 0)
        {
            pw.format("flops / nnz(L):  %8.1f\n", cmfl / cmlnz) ;
        }
        if (anz > 0)
        {
            pw.format("nnz(L) / nnz(A): %8.1f\n", cmlnz / anz) ;
        }
        pw.format("analyze cputime:  %12.4f\n", timeRecord.getAnalyze());
        double tf = timeRecord.getFactorize();
        pw.format("factor  cputime:   %12.4f mflop: %8.1f\n", tf,
                (tf == 0) ? 0 : (1e-6 * cmfl / tf));

        double ts0 = timeRecord.getSolveMethod0();
        pw.format("solve   cputime:   %12.4f mflop: %8.1f\n", ts0,
                (ts0 == 0) ? 0 : (1e-6*4*cmlnz / ts0)) ;
        double tot = timeRecord.getSolveMethod2() + tf + ts0;
        pw.format("overall cputime:   %12.4f mflop: %8.1f\n",
                tot, (tot == 0) ? 0 : (1e-6 * (cmfl + 4 * cmlnz) / tot));
        double ts1 = timeRecord.getSolveMethod1();
        pw.format("solve   cputime:   %12.4f mflop: %8.1f (%d trials)\n", ts1,
                (ts1 == 0) ? 0 : (1e-6 * 4 * cmlnz / ts1), NTRIALS);
        double ts2 = timeRecord.getSolveMethod2();
        pw.format("solve2  cputime:   %12.4f mflop: %8.1f (%d trials)\n", ts2,
                (ts2 == 0) ? 0 : (1e-6*4*cmlnz / ts2), NTRIALS);
        pw.format("peak memory usage: %12.0f (MB)\n",
                cm.memory_usage.doubleValue() / 1048576.);
        pw.print("residual (|Ax-b|/(|A||x|+|b|)): ") ;
        for (int method = 0 ; method <= 3 ; method++)
        {
            pw.format("%8.2e ", resid [method]) ;
        }
        pw.println();
        double resid2 = resid[4];
        if (resid2 >= 0)
        {
            pw.format("residual %8.1e (|Ax-b|/(|A||x|+|b|)) after iterative refinement\n",
                    resid2) ;
        }
        pw.format("rcond    %8.1e\n\n", rcond) ;

        if (l_is_super != 0)
        {
            pw.flush();
            check.cholmod_gpu_stats (cm);
            jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));
        }
    }

    /* ---------------------------------------------------------------------- */
    /* iterative refinement (real symmetric case only) */
    /* ---------------------------------------------------------------------- */
    private double iterativeRefinement(cholmod_factor L, cholmod_dense X, Residual robj)
    {
        double resid2 = -1;
        cholmod_dense R = robj.getR();
        if (A.stype.get() != cholmod_sparse.SType.UNSYMMETRIC && aXtype == CHOLMOD_REAL)
        {
            cholmod_dense R2;

            /* R2 = A\(B-A*X) */
            R2 = chol.cholmod_solve(CHOLMOD_A, L, R, cm);
            /* compute X = X + A\(B-A*X) */
            Pointer pXx = X.x.get();
            double[] Xx = new double[nrow];
            double[] Rx = new double[nrow];
            pXx.get(0, Xx, 0, nrow);
            R.x.get().get(0, Rx, 0, nrow);

            for (int i = 0; i < nrow; i++)
            {
                Xx[i] = Xx[i] + Rx[i];
            }

            pXx.put(0, Xx, 0, nrow);
            Core.Cholmod_Free_Dense(core, R2, cm);
            Core.Cholmod_Free_Dense(core, R, cm);
            /* compute the new residual, R = B-A*X */
            R = core.cholmod_copy_dense(B, cm);
            mops.cholmod_sdmult(A, 0, minusone, one, X, R, cm);
            double rnorm2 = mops.cholmod_norm_dense(R, 0, cm);
            resid2 = rnorm2 / robj.getAxbnorm();
        }

        return resid2;
    }

    private Residual computeResidual(cholmod_dense X)
    {
        cholmod_dense R = null;
        if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            /* (AA'+beta*I)x=b is the linear system that was solved */
            /* W = A'*X */
            cholmod_dense W = core.cholmod_allocate_dense(ncol, 1, ncol, aXtype, cm);
            mops.cholmod_sdmult(A, 2, one, zero, X, W, cm);
            /* R = B - beta*X */
            R = core.cholmod_zeros(nrow, 1, aXtype, cm);
            ;
            Pointer pRx = R.x.get();
            double[] Rx = new double[nrow];
            pRx.get(0, Rx, 0, nrow);
            double[] Xx = new double[nrow];
            X.x.get().get(0, Xx, 0, nrow);

            if (aXtype == CHOLMOD_REAL)
            {
                for (int i = 0; i < nrow; i++)
                {
                    Rx[i] = Bx[i] - beta[0] * Xx[i];
                }
            }
            else
            {
                /* complex case */
                for (int i = 0; i < nrow; i++)
                {
                    Rx[2 * i] = Bx[2 * i] - beta[0] * Xx[2 * i];
                    Rx[2 * i + 1] = Bx[2 * i + 1] - beta[0] * Xx[2 * i + 1];
                }
            }
            pRx.put(0, Rx, 0, nrow);
            /* R = A*W - R */
            mops.cholmod_sdmult(A, 0, one, minusone, W, R, cm);
            Core.Cholmod_Free_Dense(core, W, cm);
        }
        else
        {
            /* Ax=b was factorized and solved, R = B-A*X */
            R = core.cholmod_copy_dense(B, cm);
            mops.cholmod_sdmult(A, 0, minusone, one, X, R, cm);
        }
        double rnorm = mops.cholmod_norm_dense(R, 0, cm);        /* max abs. entry */
        double xnorm = mops.cholmod_norm_dense(X, 0, cm);        /* max abs. entry */
        double axbnorm = (anorm * xnorm + bnorm + ((nrow == 0) ? 1 : 0));
        return new Residual(R, axbnorm, rnorm / axbnorm);
    }

    private cholmod_dense solve(cholmod_dense B, cholmod_factor L, double[] resid) throws IOException
    {
        Residual robj = null;
        cholmod_dense X = null;
        for (int method = 0; method <= 3; method++)
        {
            resid[method] = -1;       /* not yet computed */
            boolean doComputeResidual = true;
            switch (method)
            {
                case 0:
                    X = basic_solve(L, B);
                    break;
                case 1:
                    X = basic_solve_multiple(L, B);
                    break;
                case 2:
                    X = solve_reused_workspace(L, B);
                    break;
                default:
                    X = solve_resusedws_sparseBset(L, B, resid);
                    doComputeResidual = false;
                    break;
            }
            if (doComputeResidual)
            {
                robj = computeResidual(X);
                resid[method] = robj.getResid();
            }
        }
        resid[4] = iterativeRefinement(L, X, robj);
        robj.cleanup();
        return X;
    }

    private cholmod_dense solve_resusedws_sparseBset(cholmod_factor L, cholmod_dense B, double[] resid) throws IOException
    {
        PointerByReference Xptr = new PointerByReference();
        PrintWriter timelog = new PrintWriter(new BufferedWriter(new FileWriter("timelog.m")));
        try
        {
            resid[3] = performSolveMethod3(Xptr, timelog, L);
        } finally
        {
            timelog.close();
        }

        cholmod_dense X = new cholmod_dense();
        X.useMemory(Xptr.getValue());
        return X;
    }

    private double performSolveMethod3(PointerByReference Xptr, PrintWriter timelog, cholmod_factor L)
    {
        PointerByReference Ywork = new PointerByReference();
        PointerByReference Ework = new PointerByReference();
        XType xtype = aXtype;
        timelog.println("results=[");
        cholmod_dense B2 = core.cholmod_zeros(nrow, 1, xtype, cm);
        double[] B2x = new double[nrow];
        Pointer pB2x = B2.x.get();
        pB2x.get(0, B2x, 0, nrow);
        cholmod_sparse Bset = core.cholmod_allocate_sparse(nrow, 1, 1, 0, 1,
                cholmod_sparse.SType.UNSYMMETRIC, CHOLMOD_PATTERN, cm);
        Pointer pBseti = Bset.i.get();

        Bset.p.get().put(0, new int[]{0, 1}, 0, 2);
        double resid = 0;
        cholmod_dense X = new cholmod_dense();
        PointerByReference pX2 = new PointerByReference();
        PointerByReference pXset = new PointerByReference();
        for (int i = 0; i < Math.min(100, nrow); ++i)
        {
            /* B (i) is nonzero, all other entries are ignored
               (implied to be zero) */
            pBseti.putInt(0, i);
            double di = (double) i;
            if (xtype == CHOLMOD_REAL)
            {
                B2x[i] = 3.1 * di + 0.9;
            }
            else
            {
                B2x[2 * i] = di + 0.042;
                B2x[2 * i + 1] = di - 92.7;
            }

            pB2x.put(0, B2x, 0, nrow);
            /* first get the entire solution, to compare against */
            chol.cholmod_solve2(CHOLMOD_A, L, B2, null, Xptr, null,
                    Ywork, Ework, cm);

            /* now get the sparse solutions; this will change L from
               supernodal to simplicial */

            if (i == 0)
            {
                /* first solve can be slower because it has to allocate
                   space for X2, Xset, etc, and change L.
                   So don't time it */
                chol.cholmod_solve2(CHOLMOD_A, L, B2, Bset, pX2, pXset,
                        Ywork, Ework, cm);
            }

            double t = cfg.SuiteSparse_time();
            for (int trial = 0; trial < NTRIALS; trial++)
            {
                /* solve Ax=b but only to get x(i).
                   b is all zero except for b(i).
                   This takes O(xlen) time */
                chol.cholmod_solve2(CHOLMOD_A, L, B2, Bset, pX2, pXset,
                        Ywork, Ework, cm);
            }
            timeRecord.setSolveMethod3(t);

            /* check the solution and log the time */
            cholmod_sparse Xset = new cholmod_sparse();
            Xset.useMemory(pXset.getValue());
            int xlen = Xset.p.get().getInt(Integer.BYTES/* offset 1 int */);
            int[] Xseti = new int[xlen];
            Xset.i.get().get(0, Xseti, 0, xlen);
            int[] Lnz = new int[ncol];
            L.nz.get().get(0, Lnz, 0, ncol);
            double[] X1x = new double[nrow];
            double[] X2x = new double[nrow];
            cholmod_dense X2 = new cholmod_dense();
            X.useMemory(Xptr.getValue());
            X2.useMemory(pX2.getValue());
            X.x.get().get(0, X1x, 0, nrow);
            X2.x.get().get(0, X2x, 0, nrow);
            int fl;
            if (xtype == CHOLMOD_REAL)
            {
                fl = 2 * xlen;
                for (int k = 0; k < xlen; k++)
                {
                    int j = Xseti[k];
                    fl += 4 * Lnz[j];
                    double err = X1x[j] - X2x[j];
                    err = Math.abs(err);
                    resid = Math.max(resid, err);
                }
            }
            else
            {
                fl = 16 * xlen;
                for (int k = 0; k < xlen; k++)
                {
                    int j = Xseti[k];
                    fl += 16 * Lnz[j];
                    double err = X1x[2 * j] - X2x[2 * j];
                    err = Math.abs(err);
                    resid = Math.max(resid, err);
                    err = X1x[2 * j + 1] - X2x[2 * j + 1];
                    err = Math.abs(err);
                    resid = Math.max(resid, err);
                }
            }
            timelog.format("%d %d %d %f\n",
                    i, xlen, fl, t);
            /* clear B for the next test */
            if (xtype == CHOLMOD_REAL)
            {
                B2x[i] = 0.0;
            }
            else
            {
                B2x[2 * i] = 0.0;
                B2x[2 * i + 1] = 0.0;
            }
        }

        timelog.format("] ; resid = %g ;\n", resid);
        timelog.format("lnz = %g ;\n", cm.lnz.doubleValue());
        timelog.format("t = %g ;   %% dense solve time\n", timeRecord.getSolveMethod2());

        resid /= mops.cholmod_norm_dense(X, 1, cm);

        core.cholmod_free_dense(Ywork, cm);
        core.cholmod_free_dense(Ework, cm);
        core.cholmod_free_dense(pX2, cm);
        Core.Cholmod_Free_Dense(core, B2, cm);
        core.cholmod_free_sparse(pXset, cm);
        Core.Cholmod_Free_Sparse(core, Bset, cm);
        return resid;
    }

    private cholmod_dense solve_reused_workspace(cholmod_factor L, cholmod_dense B)
    {
        PointerByReference Ywork = new PointerByReference();
        PointerByReference Ework = new PointerByReference();
        PointerByReference Xptr = new PointerByReference();
        Pointer pBx = B.x.get();
        double x = (double) nrow;
        double t = cfg.SuiteSparse_time();
        for (int trial = 1; trial < NTRIALS; ++trial)
        {
            pBx.putDouble(0, ((double) 1 + trial) / x);
            chol.cholmod_solve2(CHOLMOD_A, L, B, null, Xptr, null, Ywork, Ework, cm);
        }
        core.cholmod_free_dense(Ywork, cm);
        core.cholmod_free_dense(Ework, cm);
        timeRecord.setSolveMethod2(t);
        cholmod_dense X = new cholmod_dense();
        X.useMemory(Xptr.getValue());
        return X;
    }

    private cholmod_dense basic_solve_multiple(cholmod_factor L, cholmod_dense B)
    {
        Pointer pBx = B.x.get();
        double x = (double) nrow;
        double t = cfg.SuiteSparse_time();
        pBx.putDouble(0, 1.0 / x);
        cholmod_dense X = chol.cholmod_solve(CHOLMOD_A, L, B, cm);
        for (int trial = 1; trial < NTRIALS; ++trial)
        {
            Core.Cholmod_Free_Dense(core, X, cm);
            pBx.putDouble(0, ((double) 1 + trial) / x);
            X = chol.cholmod_solve(CHOLMOD_A, L, B, cm);
        }
        timeRecord.setSolveMethod1(t);
        return X;
    }

    private cholmod_dense basic_solve(cholmod_factor L, cholmod_dense B)
    {
        double t = cfg.SuiteSparse_time();
        cholmod_dense X = chol.cholmod_solve(CHOLMOD_A, L, B, cm);
        timeRecord.setSolveMethod0(t);
        return X;
    }

    private FactorParms analyzeFactorize()
    {
        double t = cfg.SuiteSparse_time();
        cholmod_factor L = chol.cholmod_analyze(A, cm);
        timeRecord.setAnalyze(t);
        pw.format("Analyze: flop %g lnz %g\n", cm.fl.doubleValue(), cm.lnz.doubleValue());

        if (A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            pw.println("Factorizing A*A'+beta*I)");
            double tf = cfg.SuiteSparse_time();
            chol.cholmod_factorize_p(A, beta, null, 0, L, cm);
            timeRecord.setFactorize(tf);
        }
        else
        {
            pw.println("Factorizing A");
            double tf = cfg.SuiteSparse_time();
            chol.cholmod_factorize(A, L, cm);
            timeRecord.setFactorize(tf);
        }
        pw.flush();
        check.cholmod_print_factor(L, "L", cm);
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));

        return new FactorParms(L);
    }

    private void createRightHandSide()
    {
        XType xtype = aXtype;
        B = core.cholmod_zeros(nrow, 1, xtype, cm);
        Bx = new double[nrow];
        if (xtype == CHOLMOD_REAL)
        {
            /* real case */
            for (int i = 0; i < nrow; i++)
            {
                double x = nrow;
                Bx[i] = 1 + i / x;
            }
        }
        else
        {
            /* complex case */
            for (int i = 0; i < nrow; i++)
            {
                double x = nrow;
                Bx[2 * i] = 1 + i / x;        /* real part of B(i) */
                Bx[2 * i + 1] = (x / 2 - i) / (3 * x);    /* imag part of B(i) */
            }
        }
        B.x.get().put(0, Bx, 0, nrow);
        pw.flush();
        check.cholmod_print_dense(B, "B", cm);
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));
        bnorm = mops.cholmod_norm_dense(B, 0, cm);
        pw.format("bnorm %g\n", bnorm);
    }

    private void prep()
    {
        anorm = mops.cholmod_norm_sparse(A, 0, cm);
        pw.format("norm (A,inf) = %g\n", anorm);
        pw.format("norm (A,1)   = %g\n", mops.cholmod_norm_sparse(A, 1, cm));
        pw.flush();
        check.cholmod_print_sparse(A, "A", cm);
        jle.fflush(JnrLibcExtra.StdioStream(jle.stdout()));

        nrow = A.nrow.intValue();
        ncol = A.ncol.intValue();
        if (nrow > ncol)
        {
            /* Transpose A so that A'A+beta*I will be factorized instead */
            cholmod_sparse C = core.cholmod_transpose(A, 2, cm);
            Core.Cholmod_Free_Sparse(core, A, cm);
            A = C;
            pw.println("transposing input matrix");
        }
    }


    public static void main(String... args)
    {
        File f = null;
        for (int i = 0; i < args.length; )
        {
            String s = args[i++].toLowerCase();
            int ssx = 1;
            if (s.startsWith("--")) ++ssx;
            switch (s.substring(ssx))
            {
                case "matrix":
                case "m":
                    f = new File(args[i++]);
                    break;
            }
        }

        Core core = Core.Load();
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        CholmodDemo cd = new CholmodDemo(cc, core, out);
        cc.error_handler.set((s, file, l, m) ->
        {
            System.err.format("%s %s file: %s line: %d\n",
                    s.toString(), m, file, l);
            System.exit(1);
        });
        printVersions(core);
        cholmod_sparse A = loadSparse(f, cc);
        try
        {
            cd.runDemo(A);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            out.close();
        }
        Core.Cholmod_Free_Sparse(core, A, cc);
        core.cholmod_finish(cc);
    }

    private static void printVersions(Core core)
    {
        System.out.println("\n---------------------------------- cholmod_demo:");
        Core.CholmodVersion cver = Core.getCholmodVersion(core);
        System.out.format("cholmod version %d.%d.%d\n", cver.getMain(), cver.getSub(), cver.getSubsub());

        SuiteSparse_config cfg = SuiteSparse_config.Load();
        SuiteSparseVersion gver = SuiteSparse_config.HelpVersion(cfg);
        System.out.format("SuiteSparse version %d.%d.%d\n", gver.getSuitesparseMainVersion(),
                gver.getSuitesparseSubVersion(), gver.getSuitesparseSubsubVersion());

    }

    /**
     * Take the desired matrix file and
     * load the sparse matrix using JNR
     * and SuiteSparse libraries
     *
     * @param f File object containing the path we wish to load
     * @return C FILE pointer encapsulated in a JNR pointer.  Could be
     * pointer to stdin if the file parameter is null
     */
    private static cholmod_sparse loadSparse(File f, cholmod_common cc)
    {
        JnrLibcExtra jle = JnrLibcExtra.Load();
        Pointer fp = null;
        boolean close = false;
        if (f == null)
        {
            System.out.println("No --matrix flag provided, using standard input");
            fp = JnrLibcExtra.StdioStream(jle.stdin());
        }
        else
        {
            fp = jle.fopen(f.getAbsolutePath(), "r");
            if (fp == null)
            {
                int lerr = Runtime.getSystemRuntime().getLastError();
                System.err.format("Unable to open file %s, error code %d\n", f.getAbsolutePath(), lerr);
                System.exit(1);
            }
            close = true;
        }

        Check check = Check.Load();

        cholmod_sparse A = check.cholmod_read_sparse(fp, cc);
        if (close) jle.fclose(fp);
        return A;
    }

}
