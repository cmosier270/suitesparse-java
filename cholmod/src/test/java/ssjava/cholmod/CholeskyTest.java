package ssjava.cholmod;

import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CholeskyTest
{
    private static final int nrow = 2;
    private static final int ncol = 2;
    private static final int nnz = 3;

    private static final int[] mi = {0, 1, 1};
    private static final int[] mj = {0, 0, 1};
    private static final double x[] = {3, -1, 3};

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
        T.x.get().put(0, x, 0, nnz);
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
        chol.cholmod_factorize(A, L, cc);
        cholmod_dense B = core.cholmod_ones(2, 1, XType.CHOLMOD_REAL, cc);
        cholmod_dense x = chol.cholmod_solve(SolveSystem.CHOLMOD_A, L, B, cc);
        double[] xx = new double[nrow];
        x.x.get().get(0, xx, 0, nrow );
        assertArrayEquals(new double[] {0.5,0.5}, xx);
        Core.Cholmod_Free_Factor(core, L, cc);
        Core.Cholmod_Free_Dense(core, B, cc);
        core.cholmod_finish(cc);
    }
}


