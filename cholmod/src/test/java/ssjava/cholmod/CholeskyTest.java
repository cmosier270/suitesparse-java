package ssjava.cholmod;

import jnr.ffi.byref.PointerByReference;
import org.junit.jupiter.api.Test;
import ssjava.suitesparse_config.JnrLibcExtra;

import static org.junit.jupiter.api.Assertions.*;

class CholeskyTest
{
    public static final int nrow = 2;
    public static final int ncol = 2;
    public static final int nnz = 3;

    public static final int[] mi = {0, 1, 1};
    public static final int[] mj = {0, 0, 1};
    public static final double mx[] = {3, -1, 3};

    public static final long[] lmi = {0, 1, 1};
    public static final long[] lmj = {0, 0, 1};

    private final Cholesky chol = Cholesky.Load();
    private final Core core = Core.Load();
    private final JnrLibcExtra jle = JnrLibcExtra.Load();
    private final Check check = Check.Load();

    public static cholmod_triplet getTestTriplet(Core core, cholmod_common cc)
    {
        cholmod_triplet T = core.cholmod_allocate_triplet(nrow, ncol, nnz,
                cholmod_triplet.SType.SYMMETRIC_LOWER_TRANSPOSE_UPPER,
                XType.CHOLMOD_REAL, cc);
        T.i.get().put(0, mi, 0, nnz);
        T.j.get().put(0, mj, 0, nnz);
        T.x.get().put(0, mx, 0, nnz);
        T.nnz.set(nnz);
        return T;
    }

    public static cholmod_triplet get_L_TestTriplet(Core core, cholmod_common cc)
    {
        cholmod_triplet T = core.cholmod_l_allocate_triplet(nrow, ncol, nnz,
                cholmod_triplet.SType.SYMMETRIC_LOWER_TRANSPOSE_UPPER,
                XType.CHOLMOD_REAL, cc);
        T.i.get().put(0, lmi, 0, nnz);
        T.j.get().put(0, lmj, 0, nnz);
        T.x.get().put(0, mx, 0, nnz);
        T.nnz.set(nnz);
        return T;
    }

    @Test
    void cholmod_solve()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_triplet T = getTestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, nnz, cc);
        cholmod_factor L = chol.cholmod_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_factorize(A, L, cc));
        assertEquals(0.8888888888888888, chol.cholmod_rcond(L, cc));
        cholmod_dense B = core.cholmod_ones(2, 1, XType.CHOLMOD_REAL, cc);
        cholmod_dense x = chol.cholmod_solve(SolveSystem.CHOLMOD_A, L, B, cc);
        double[] xx = new double[nrow];
        x.x.get().get(0, xx, 0, nrow );
        assertArrayEquals(new double[] {0.5,0.5}, xx);
        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Dense(core, B, cc);
        Core.Cholmod_Free_Triplet(core, T, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        Core.Cholmod_Free_Dense(core, x, cc);
        core.cholmod_finish(cc);
    }

    @Test
    void cholmod_l_solve()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_l_start(cc);
        cholmod_triplet T = get_L_TestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_l_triplet_to_sparse(T, nnz, cc);
        cholmod_factor L = chol.cholmod_l_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_l_factorize(A, L, cc));
        assertEquals(0.8888888888888888, chol.cholmod_l_rcond(L, cc));
        cholmod_dense B = core.cholmod_l_ones(2, 1, XType.CHOLMOD_REAL, cc);
        cholmod_dense x = chol.cholmod_l_solve(SolveSystem.CHOLMOD_A, L, B, cc);
        double[] xx = new double[nrow];
        x.x.get().get(0, xx, 0, nrow );
        assertArrayEquals(new double[] {0.5,0.5}, xx);
        Core.Cholmod_L_Free_Factor(core, L, cc);
        Core.Cholmod_L_Free_Dense(core, B, cc);
        Core.Cholmod_L_Free_Triplet(core, T, cc);
        Core.Cholmod_L_Free_Sparse(core, A, cc);
        Core.Cholmod_L_Free_Dense(core, x, cc);
        core.cholmod_l_finish(cc);
    }


    @Test
    void cholmod_solve2()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_triplet T = getTestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, nnz, cc);
        cholmod_factor L = chol.cholmod_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_factorize(A, L, cc));
        assertEquals(0.8888888888888888, chol.cholmod_rcond(L, cc));
        cholmod_dense B = core.cholmod_ones(2, 1, XType.CHOLMOD_REAL, cc);
        PointerByReference Xptr = new PointerByReference();
        PointerByReference Ywptr = new PointerByReference();
        PointerByReference Ewptr = new PointerByReference();
        assertEquals(1,chol.cholmod_solve2(SolveSystem.CHOLMOD_A, L, B, null, Xptr, null, Ywptr, Ewptr,cc));
        double[] xx = new double[nrow];
        cholmod_dense x = new cholmod_dense();
        x.useMemory(Xptr.getValue());
        x.x.get().get(0, xx, 0, nrow );
        assertArrayEquals(new double[] {0.5,0.5}, xx);
        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Dense(core, B, cc);
        Core.Cholmod_Free_Triplet(core, T, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        Core.Cholmod_Free_Dense(core, x, cc);
        core.cholmod_finish(cc);
    }

    @Test
    void cholmod_l_solve2()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_l_start(cc);
        cholmod_triplet T = get_L_TestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_l_triplet_to_sparse(T, nnz, cc);
        cholmod_factor L = chol.cholmod_l_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_l_factorize(A, L, cc));
        assertEquals(0.8888888888888888, chol.cholmod_l_rcond(L, cc));
        cholmod_dense B = core.cholmod_l_ones(2, 1, XType.CHOLMOD_REAL, cc);
        PointerByReference Xptr = new PointerByReference();
        PointerByReference Ywptr = new PointerByReference();
        PointerByReference Ewptr = new PointerByReference();
        assertEquals(1,chol.cholmod_l_solve2(SolveSystem.CHOLMOD_A, L, B, null, Xptr, null, Ywptr, Ewptr,cc));
        double[] xx = new double[nrow];
        cholmod_dense x = new cholmod_dense();
        x.useMemory(Xptr.getValue());
        x.x.get().get(0, xx, 0, nrow );
        assertArrayEquals(new double[] {0.5,0.5}, xx);
        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Dense(core, B, cc);
        Core.Cholmod_Free_Triplet(core, T, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        Core.Cholmod_Free_Dense(core, x, cc);
        core.cholmod_l_finish(cc);
    }
}


