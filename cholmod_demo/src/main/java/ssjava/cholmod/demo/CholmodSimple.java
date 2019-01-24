package ssjava.cholmod.demo;
/**
 * The suitesparse-java MIT licence does not apply to this file.
 *
 * This source code was translated as directly as possible from the original C
 * Code within the SuiteSparse package.  The copyright notice from original
 * code has been replicated below.
 */


/* ========================================================================== */
/* === Demo/cholmod_simple ================================================== */
/* ========================================================================== */

/* -----------------------------------------------------------------------------
 * CHOLMOD/Demo Module.  Copyright (C) 2005-2006, Timothy A. Davis
 * -------------------------------------------------------------------------- */

/* Read in a real symmetric or complex Hermitian matrix from stdin in
 * MatrixMarket format, solve Ax=b where b=[1 1 ... 1]', and print the residual.
 * Usage: cholmod_simple < matrixfile
 */

import ssjava.cholmod.*;

import java.util.Arrays;

public class CholmodSimple
{
    final Core core = Core.Load();
    final Check check = Check.Load();
    final JnrLibcExtra lce = JnrLibcExtra.Load();
    final Cholesky chol = Cholesky.Load();
    final MatrixOps mops = MatrixOps.Load();
    double norm = 0.0;

    public int runDemo()
    {
        cholmod_sparse A;
        cholmod_dense x, b, r;
        cholmod_factor L;
        double[] one = {1.0, 0.0}, m1 = {-1.0, 0.0};
        cholmod_common c = new cholmod_common();
        core.cholmod_start(c);
        A = check.cholmod_read_sparse(JnrLibcExtra.StdioStream(lce.stdin()), c);
        double[] ax = new double[3];
        A.x.get().get(0,ax,0,3);
        System.out.format("A: %s, %s\n", A.stype.get().toString(), Arrays.toString(ax));
        check.cholmod_print_sparse(A, "A",c);

        if(A == null || A.stype.get() == cholmod_sparse.SType.UNSYMMETRIC)
        {
            Core.Cholmod_Free_Sparse(core, A, c);
            core.cholmod_finish(c);
            return 0;
        }

        b = core.cholmod_ones(A.nrow.intValue(), 1, A.xtype.get(), c);
        L = chol.cholmod_analyze(A, c);
        int res = chol.cholmod_factorize(A, L, c);

        x = chol.cholmod_solve(SolveSystem.CHOLMOD_A, L, b, c);
        int ncol = A.ncol.intValue();
        double[] xx = new double[ncol];
        x.x.get().get(0, xx, 0, ncol);
        System.out.println(Arrays.toString(xx));
        r = core.cholmod_copy_dense(b, c);
        mops.cholmod_sdmult(A, 0, m1, one, x, r, c);
        norm = mops.cholmod_norm_dense(r, 0, c);
        System.out.format("norm(b-Ax) %8.1e\n", norm);
        Core.Cholmod_Free_Factor(core, L, c);
        Core.Cholmod_Free_Sparse(core, A, c);
        Core.Cholmod_Free_Dense(core, r, c);
        Core.Cholmod_Free_Dense(core, x, c);
        Core.Cholmod_Free_Dense(core, b, c);
        core.cholmod_finish(c);
        return 0;
    }


    public static void main(String... args)
    {
        (new CholmodSimple()).runDemo();
    }
}
