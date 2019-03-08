package ssjava.cholmod;

import jnr.ffi.LibraryLoader;
import jnr.ffi.annotations.In;
import jnr.ffi.provider.FFIProvider;

public interface MatrixOps
{
    static MatrixOps Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<MatrixOps> ll = provider.createLibraryLoader(MatrixOps.class);
        return ll.load("cholmod");
    }

    int cholmod_sdmult(@In cholmod_sparse A, @In int transpose, @In double[] alpha, @In double[] beta,
                       @In cholmod_dense X, cholmod_dense Y, cholmod_common cc);
    int cholmod_l_sdmult(@In cholmod_sparse A, @In int transpose, @In double[] alpha, @In double[] beta,
                       @In cholmod_dense X, cholmod_dense Y, cholmod_common cc);

    double cholmod_norm_dense (@In cholmod_dense X, @In int norm, cholmod_common cc);
    double cholmod_l_norm_dense (@In cholmod_dense X, @In int norm, cholmod_common cc);

    double cholmod_norm_sparse(@In cholmod_sparse A, @In int norm, cholmod_common cc);
    double cholmod_l_norm_sparse(@In cholmod_sparse A, @In int norm, cholmod_common cc);
}
