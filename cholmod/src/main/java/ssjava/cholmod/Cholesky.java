package ssjava.cholmod;

import jnr.ffi.LibraryLoader;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.IntByReference;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.provider.FFIProvider;
import jnr.ffi.types.size_t;

public interface Cholesky
{
    static Cholesky Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<Cholesky> ll = provider.createLibraryLoader(Cholesky.class);
        return ll.load("cholmod");
    }

    cholmod_factor cholmod_analyze(@In cholmod_sparse A, cholmod_common cc);

    cholmod_factor cholmod_l_analyze(@In cholmod_sparse A, cholmod_common cc);

    int cholmod_factorize(@In cholmod_sparse A, cholmod_factor L, cholmod_common cc);

    int cholmod_l_factorize(@In cholmod_sparse A, cholmod_factor L, cholmod_common cc);

    cholmod_dense cholmod_solve(@In SolveSystem sys, @In cholmod_factor L, @In cholmod_dense B, cholmod_common cc);

    cholmod_dense cholmod_l_solve(@In SolveSystem sys, @In cholmod_factor L, @In cholmod_dense B, cholmod_common cc);

    int cholmod_solve2     /* returns TRUE on success, FALSE on failure */
    (
            /* ---- input ---- */
            @In SolveSystem sys,                    /* system to solve */
            @In cholmod_factor L,                /* factorization to use */
            @In cholmod_dense B,               /* right-hand-side */
            @In cholmod_sparse Bset,
            /* ---- output --- */
            PointerByReference X_Handle,        /* (cholmod_dense**) solution, allocated if need be */
            PointerByReference Xset_Handle,     /* (cholmod_sparse**) */
            /* ---- workspace  */
            PointerByReference Y_Handle,       /* (cholmod_dense **) workspace, or NULL */
            PointerByReference E_Handle,       /* (cholmod_dense **) workspace, or NULL */
            /* --------------- */
            cholmod_common cc);

    int cholmod_l_solve2
    (
            @In SolveSystem sys,
            @In cholmod_factor L,
            @In cholmod_dense B,
            @In cholmod_sparse Bset,
            PointerByReference X_Handle,
            PointerByReference Xset_Handle,
            PointerByReference Y_Handle,
            PointerByReference E_Handle,
            cholmod_common cc);

    int cholmod_factorize_p(@In cholmod_sparse A, @In double[] beta /* size 2 */, IntByReference fset,
                            @In @size_t long fsize, cholmod_factor L, cholmod_common cc);

    int cholmod_l_factorize_p(@In cholmod_sparse A, @In double[] beta /* size 2 */, IntByReference fset,
                              @In @size_t long fsize, cholmod_factor L, cholmod_common cc);

    double cholmod_rcond(@In cholmod_factor L, cholmod_common cc);

    double cholmod_l_rcond(@In cholmod_factor L, cholmod_common cc);

}
