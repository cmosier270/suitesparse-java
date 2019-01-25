package ssjava.cholmod;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.provider.FFIProvider;

public interface Check
{
    static Check Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<Check> ll = provider.createLibraryLoader(Check.class);
        return ll.load("cholmod");
    }

    cholmod_sparse cholmod_read_sparse(@In Pointer  /* (FILE *) */ f, cholmod_common cc);
    cholmod_sparse cholmod_l_read_sparse(@In Pointer /* (FILE *) */ f, cholmod_common cc);
    int cholmod_print_sparse(@In cholmod_sparse A, @In String name, cholmod_common cc);
    int cholmod_l_print_sparse(@In cholmod_sparse A, @In String name, cholmod_common cc);

    cholmod_triplet cholmod_read_triplet(@In Pointer /* (FILE *) */ f, cholmod_common cc);
    cholmod_triplet cholmod_l_read_triplet(@In Pointer /* (FILE *) */ f, cholmod_common cc);



    int cholmod_print_triplet(@In cholmod_triplet T, @In String name, cholmod_common cc);

}
