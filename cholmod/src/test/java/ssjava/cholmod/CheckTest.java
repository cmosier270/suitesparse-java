package ssjava.cholmod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTest
{
    private final Check check = Check.Load();
    private final Core core = Core.Load();
    private final Cholesky chol = Cholesky.Load();

    @Test
    void print_dense()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_dense X = core.cholmod_ones(3,25,XType.CHOLMOD_REAL,cc);
        assertEquals(1, check.cholmod_print_dense(X, "X", cc));
        Core.Cholmod_Free_Dense(core, X, cc);
        core.cholmod_finish(cc);
    }

    @Test
    void print_l_dense()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_l_start(cc);
        cholmod_dense X = core.cholmod_l_ones(3,25,XType.CHOLMOD_REAL,cc);
        assertEquals(1, check.cholmod_l_print_dense(X, "X", cc));
        Core.Cholmod_L_Free_Dense(core, X, cc);
        core.cholmod_l_finish(cc);
    }

    @Test
    void print_factor()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_start(cc);
        cholmod_triplet T = CholeskyTest.getTestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_triplet_to_sparse(T, T.nzmax.longValue(), cc);
        cholmod_factor L = chol.cholmod_analyze(A,cc);
        assertEquals(1, check.cholmod_print_factor(L, "L", cc));
        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Sparse(core, A, cc);
        Core.Cholmod_Free_Triplet(core, T, cc);
        core.cholmod_finish(cc);
    }
    @Test
    void print_l_factor()
    {
        cholmod_common cc = new cholmod_common();
        core.cholmod_l_start(cc);
        cholmod_triplet T = CholeskyTest.get_L_TestTriplet(core, cc);
        cholmod_sparse A = core.cholmod_l_triplet_to_sparse(T, T.nzmax.longValue(), cc);
        cholmod_factor L = chol.cholmod_l_analyze(A,cc);
        assertEquals(1, check.cholmod_l_print_factor(L, "L", cc));
        Core.Cholmod_L_Free_Factor(core, L, cc);
        Core.Cholmod_L_Free_Sparse(core, A, cc);
        Core.Cholmod_L_Free_Triplet(core, T, cc);
        core.cholmod_l_finish(cc);
    }
}