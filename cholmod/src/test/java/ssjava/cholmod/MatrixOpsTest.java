package ssjava.cholmod;

import org.junit.jupiter.api.Test;
import ssjava.suitesparse_config.JnrLibcExtra;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixOpsTest
{
    public static final Core CORE = Core.Load();
    public static final MatrixOps MATRIX_OPS = MatrixOps.Load();
    private static final JnrLibcExtra JLE = JnrLibcExtra.Load();

    private static final double[] ALPHA = {1.0,0.0};
    private static final double[] BETA = {0.0, 0.0};
    private static final double[] RESULT = {3.0, -1.0, 4.0, 0.0, 47.0, 1.0};

    @Test
    void cholmod_sdmult()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletI(CORE, cc);
        cholmod_sparse A = CORE.cholmod_triplet_to_sparse(T, T.getNZMax(), cc);
        cholmod_dense x = CORE.cholmod_ones(A.getNCol(), 1, T.getXType(), cc);
        int nrow = T.getNRow();
        cholmod_dense Y = CORE.cholmod_allocate_dense(nrow, 1, nrow, T.getXType(), cc);
        MATRIX_OPS.cholmod_sdmult(A, 0, ALPHA, BETA, x, Y, cc);

        assertArrayEquals(RESULT, Y.getDoubleValues().getX());

        Core.Cholmod_Free_Dense(CORE, Y, cc);
        Core.Cholmod_Free_Dense(CORE, x, cc);
        Core.Cholmod_Free_Sparse(CORE, A, cc);
        Core.Cholmod_Free_Triplet(CORE, T, cc);
        CORE.cholmod_finish(cc);
    }

    @Test
    void cholmod_l_sdmult()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_l_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletL(CORE, cc);
        cholmod_sparse A = CORE.cholmod_l_triplet_to_sparse(T, T.getNZMax(), cc);
        cholmod_dense x = CORE.cholmod_l_ones(A.getNCol(), 1, T.getXType(), cc);
        int nrow = T.getNRow();
        cholmod_dense Y = CORE.cholmod_l_allocate_dense(nrow, 1, nrow, T.getXType(), cc);
        MATRIX_OPS.cholmod_l_sdmult(A, 0, ALPHA, BETA, x, Y, cc);

        assertArrayEquals(RESULT, Y.getDoubleValues().getX());

        Core.Cholmod_L_Free_Dense(CORE, Y, cc);
        Core.Cholmod_L_Free_Dense(CORE, x, cc);
        Core.Cholmod_L_Free_Sparse(CORE, A, cc);
        Core.Cholmod_L_Free_Triplet(CORE, T, cc);
        CORE.cholmod_l_finish(cc);
    }

    private final static double EXPECTED_NORM = 47.236;
    @Test
    void norm_dense()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletI(CORE, cc);
        cholmod_sparse A = CORE.cholmod_triplet_to_sparse(T, T.getNZMax(), cc);
        cholmod_dense X = CORE.cholmod_sparse_to_dense(A, cc);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_norm_dense(X, 1, cc), 1e-3);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_norm_dense(X, 0, cc), 1e-3);
        Core.Cholmod_Free_Dense(CORE, X, cc);
        Core.Cholmod_Free_Sparse(CORE, A, cc);
        Core.Cholmod_Free_Triplet(CORE, T, cc);
        CORE.cholmod_finish(cc);
    }

    @Test
    void norm_dense_l()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_l_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletL(CORE, cc);
        cholmod_sparse A = CORE.cholmod_l_triplet_to_sparse(T, T.getNZMax(), cc);
        cholmod_dense X = CORE.cholmod_l_sparse_to_dense(A, cc);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_l_norm_dense(X, 1, cc), 1e-3);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_l_norm_dense(X, 0, cc), 1e-3);
        Core.Cholmod_L_Free_Dense(CORE, X, cc);
        Core.Cholmod_L_Free_Sparse(CORE, A, cc);
        Core.Cholmod_L_Free_Triplet(CORE, T, cc);
        CORE.cholmod_l_finish(cc);
    }

    @Test
    void norm_sparse()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletI(CORE, cc);
        cholmod_sparse A = CORE.cholmod_triplet_to_sparse(T,T.getNZMax(),cc);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_norm_sparse(A, 0, cc), 1e-3);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_norm_sparse(A, 1, cc), 1e-3);
        Core.Cholmod_Free_Sparse(CORE, A, cc);
        Core.Cholmod_Free_Triplet(CORE, T, cc);
        CORE.cholmod_finish(cc);
    }

    @Test
    void norm_sparse_l()
    {
        cholmod_common cc = new cholmod_common();
        CORE.cholmod_l_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTripletL(CORE, cc);
        cholmod_sparse A = CORE.cholmod_l_triplet_to_sparse(T,T.getNZMax(),cc);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_l_norm_sparse(A, 0, cc), 1e-3);
        assertEquals(EXPECTED_NORM, MATRIX_OPS.cholmod_l_norm_sparse(A, 1, cc), 1e-3);
        Core.Cholmod_L_Free_Sparse(CORE, A, cc);
        Core.Cholmod_L_Free_Triplet(CORE, T, cc);
        CORE.cholmod_l_finish(cc);
    }
}