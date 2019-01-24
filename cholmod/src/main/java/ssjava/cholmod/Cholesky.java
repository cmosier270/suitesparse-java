package ssjava.cholmod;

import jnr.ffi.LibraryLoader;
import jnr.ffi.annotations.In;
import jnr.ffi.provider.FFIProvider;

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
    cholmod_dense cholmod_solve(@In SolveSystem sys, @In cholmod_factor L, @In cholmod_dense B , cholmod_common cc);
    cholmod_dense cholmod_l_solve(@In SolveSystem sys, @In cholmod_factor L, @In cholmod_dense B , cholmod_common cc);
}
