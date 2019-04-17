package ssjava.cholmod;

import jnr.ffi.Pointer;
import jnr.ffi.byref.PointerByReference;
import org.junit.jupiter.api.Test;
import ssjava.suitesparse_config.JnrLibcExtra;

import static org.junit.jupiter.api.Assertions.*;

class CholeskyTest
{
    private final Cholesky chol = Cholesky.Load();
    private final Core core = Core.Load();
    private final JnrLibcExtra jle = JnrLibcExtra.Load();
    private final Check check = Check.Load();

    public static class TestMatrix
    {
        private final Core core;
        private final cholmod_common cc;

        public static final int nrow = 3;
        public static final int ncol = 3;
        public static final int[] rowsi = {0, 0, 1, 1, 2};
        public static final long[] rowsl = {0, 0, 1, 1, 2};
        public static final int[] colsi = {0, 2, 1, 2, 2};
        public static final long[] colsl = {0, 2, 1, 2, 2};
        public static final double[] vals = {1, 0, 2, -1, 1, 0, 3, 0, 42, 0};

        public TestMatrix(Core core, cholmod_common cc)
        {
            this.core = core;
            this.cc = cc;
        }

        public cholmod_triplet loadTestI()
        {
            int nnz = vals.length/2;
            cholmod_triplet T = core.cholmod_allocate_triplet(nrow, ncol, nnz,
                    cholmod_triplet.SType.SYMMETRIC_UPPER_TRANSPOSE_LOWER, XType.CHOLMOD_COMPLEX, cc);

            T.setColIndices(colsi);
            T.setRowIndices(rowsi);
            T.setValues(vals, null);
            T.setNNZ(nnz);
            return T;
        }
        public cholmod_triplet loadTestL()
        {
            int nnz = vals.length/2;
            cholmod_triplet T = core.cholmod_l_allocate_triplet(nrow, ncol, nnz,
                    cholmod_triplet.SType.SYMMETRIC_UPPER_TRANSPOSE_LOWER, XType.CHOLMOD_COMPLEX, cc);

            T.setColIndices(colsl);
            T.setRowIndices(rowsl);
            T.setValues(vals, null);
            T.setNNZ(nnz);
            return T;
        }
    }

    public static cholmod_triplet getTestTripletI(Core core, cholmod_common cholmod_common)
    {
        return new TestMatrix(core, cholmod_common).loadTestI();
    }

    public static cholmod_triplet getTestTripletL(Core core, cholmod_common cholmod_common)
    {
        return new TestMatrix(core, cholmod_common).loadTestL();
    }

    private static final double[] SOLVED_VALS = {1.3214285714285714, -0.07142857142857142, 1.4285714285714286,
            0.10714285714285714, -0.14285714285714285, -0.03571428571428571};

    private static final double RCOND = 0.03571428571428571;

    @Test
    void cholmod_solve()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_triplet T = getTestTripletI(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, T.getNNZ(), cc);
        cholmod_factor L = chol.cholmod_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_factorize(A, L, cc));
        assertEquals(RCOND, chol.cholmod_rcond(L, cc));
        cholmod_dense B = core.cholmod_ones(T.getNRow(), 1, XType.CHOLMOD_COMPLEX, cc);
        cholmod_dense x = chol.cholmod_solve(SolveSystem.CHOLMOD_A, L, B, cc);
        assertArrayEquals(SOLVED_VALS, x.getDoubleValues().getX());
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
        cholmod_triplet T = getTestTripletL(core, cc);
        cholmod_sparse A = core.cholmod_l_triplet_to_sparse(T, T.getNNZ(), cc);
        cholmod_factor L = chol.cholmod_l_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_l_factorize(A, L, cc));
        assertEquals(RCOND, chol.cholmod_l_rcond(L, cc));
        cholmod_dense B = core.cholmod_l_ones(T.getNRow(), 1, XType.CHOLMOD_COMPLEX, cc);
        cholmod_dense x = chol.cholmod_l_solve(SolveSystem.CHOLMOD_A, L, B, cc);
        assertArrayEquals(SOLVED_VALS, x.getDoubleValues().getX());
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
        cholmod_triplet T = getTestTripletI(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, T.getNNZ(), cc);
        cholmod_factor L = chol.cholmod_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_factorize(A, L, cc));
        assertEquals(RCOND, chol.cholmod_rcond(L, cc));
        cholmod_dense B = core.cholmod_ones(T.getNRow(), 1, XType.CHOLMOD_COMPLEX, cc);
        PointerByReference Xptr = new PointerByReference();
        PointerByReference Ywptr = new PointerByReference();
        PointerByReference Ewptr = new PointerByReference();
        assertEquals(1,chol.cholmod_solve2(SolveSystem.CHOLMOD_A, L, B, null, Xptr, null, Ywptr, Ewptr,cc));
        cholmod_dense x = new cholmod_dense();
        x.useMemory(Xptr.getValue());
        assertArrayEquals(SOLVED_VALS, x.getDoubleValues().getX());
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
        cholmod_triplet T = getTestTripletL(core, cc);
        cholmod_sparse A = core.cholmod_l_triplet_to_sparse(T, T.getNNZ(), cc);
        cholmod_factor L = chol.cholmod_l_analyze(A, cc);
        assertNotEquals(null, L);
        assertEquals(1, chol.cholmod_l_factorize(A, L, cc));
        assertEquals(RCOND, chol.cholmod_l_rcond(L, cc));
        cholmod_dense B = core.cholmod_l_ones(T.getNRow(), 1, XType.CHOLMOD_COMPLEX, cc);
        PointerByReference Xptr = new PointerByReference();
        PointerByReference Ywptr = new PointerByReference();
        PointerByReference Ewptr = new PointerByReference();
        assertEquals(1,chol.cholmod_l_solve2(SolveSystem.CHOLMOD_A, L, B, null, Xptr, null, Ywptr, Ewptr,cc));
        cholmod_dense x = new cholmod_dense();
        x.useMemory(Xptr.getValue());
        assertArrayEquals(SOLVED_VALS, x.getDoubleValues().getX());
        Core.Cholmod_L_Free_Factor(core, L, cc);
        Core.Cholmod_L_Free_Dense(core, B, cc);
        Core.Cholmod_L_Free_Triplet(core, T, cc);
        Core.Cholmod_L_Free_Sparse(core, A, cc);
        Core.Cholmod_L_Free_Dense(core, x, cc);
        core.cholmod_l_finish(cc);
    }

    @Test
    void cholmod_factor_test()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_triplet T = getTestTripletI(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, T.getNZMax(), cc);
        cholmod_factor L = chol.cholmod_analyze(A, cc);
        chol.cholmod_factorize(A, L, cc);

        int n = TestMatrix.ncol;
        assertEquals(n, L.getN());
        assertEquals(n, L.getNRow());
        assertEquals(n, L.getNCol());
        assertEquals(n, L.getMinor());
        assertNotNull(L.getPerm());
        assertNotNull(L.getColCount());
        assertNull(L.getIPerm());
        assertNull(L.getZ());
        assertEquals(A.getNZMax(), L.getNZMax());
        assertArrayEquals(new double[] {1.0, 0.0, 3.0, -0.0, 1.0, 0.0, 2.0, 1.0, 28.0, 0.0}, L.getDoubleValues().getX());
        assertArrayEquals(new int[]{0, 2, 4, 5}, L.getColumnPointersAsInt());
        assertArrayEquals(new int[]{0, 2, 1, 2, 2}, L.getRowIndicesAsInt());
        assertArrayEquals(new int[]{2, 2, 1}, L.getColumnNonZeros());
        assertNotNull(L.getNext());
        assertNotNull(L.getPrev());
        assertEquals(0, L.getMaxcsize());
        assertEquals(0, L.getMaxesize());
        assertNull(L.getSuper());
        assertNull(L.getPi());
        assertNull(L.getPx());
        assertNull(L.getS());
        assertEquals(Order.CHOLMOD_AMD, L.getOrdering());
        assertFalse(L.isLL());
        assertTrue(L.isMonotonic());
        assertEquals(cc.getUseGPU(), L.getUseGPU());
        assertTrue(L.isSorted());
        assertEquals(0, L.getNSuper());
        assertEquals(0, L.getSSize());
        assertEquals(0, L.getXSize());
        assertFalse(L.isSuper());
        assertEquals(T.getDType(), L.getDType());



        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        Core.Cholmod_Free_Triplet(core, T, cc);
        core.cholmod_finish(cc);
    }

    @Test void cholmod_factorize_p()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        Pointer fp = jle.fopen(getClass().getResource("/up.tri").getFile(), "r");
        cholmod_sparse A = check.cholmod_read_sparse(fp, cc);
        jle.fclose(fp);

        cholmod_factor L = chol.cholmod_analyze(A, cc);
        assertEquals(1, chol.cholmod_factorize_p(A, new double[]{1e-6, 0}, null, 0, L, cc));

        assertArrayEquals(new double[]{1, 0, 3, 0, 1, 0, 2, 1, 28, 0}, L.getDoubleValues().getX(), 1e-4);

        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        core.cholmod_finish(cc);
    }

    @Test
    void cholmod_l_factorize_p()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_l_start(cc);
        Pointer fp = jle.fopen(getClass().getResource("/up.tri").getFile(), "r");
        cholmod_sparse A = check.cholmod_l_read_sparse(fp, cc);
        jle.fclose(fp);

        cholmod_factor L = chol.cholmod_l_analyze(A, cc);
        assertEquals(1, chol.cholmod_l_factorize_p(A, new double[]{1e-6, 0}, null, 0, L, cc));

        assertArrayEquals(new double[]{1, 0, 3, 0, 1, 0, 2, 1, 28, 0}, L.getDoubleValues().getX(), 1e-4);

        Core.Cholmod_L_Free_Factor(core, L, cc);
        Core.Cholmod_L_Free_Sparse(core, A, cc);
        core.cholmod_l_finish(cc);
    }

}


