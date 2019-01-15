package ssjava.suitesparse_config;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.DoubleByReference;
import jnr.ffi.byref.IntByReference;
import jnr.ffi.provider.FFIProvider;
import jnr.ffi.types.size_t;

/**
 * Maps methods for SuiteSparse_config
 */
public interface SuiteSparse_config
{
    void SuiteSparse_start();
    void SuiteSparse_finish();
    Pointer SuiteSparse_malloc(@In @size_t int nitems, @In @size_t int size_of_item);
    Pointer SuiteSparse_calloc(@In @size_t int nitems, @In @size_t int size_of_item);
    Pointer SuiteSparse_realloc(@In @size_t int nitems_new, @In @size_t int nitems_old,
        @In @size_t int size_of_item, Pointer p, @Out IntByReference ok);
    Pointer SuiteSparse_free(@In Pointer p);

    /**
     * See HelpTic method to handle allocation as a convenience
     * @param tic double array length 2
     */
    void SuiteSparse_tic(@In Pointer tic);
    double SuiteSparse_toc(@In Pointer toc);
    double SuiteSparse_time();

    double SuiteSparse_hypot(@In double x, @In double y);
    int SuiteSparse_divcomplex(@In double ar, @In double ai,
                               @In double br, @In double bi,
                               @Out DoubleByReference cr,
                               @Out DoubleByReference ci);

    int SuiteSparse_version(@Out int[] version);

    public static SuiteSparse_config Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<SuiteSparse_config> ll = provider.createLibraryLoader(SuiteSparse_config.class);
        return ll.load("suitesparseconfig");
    }

    /**
     * Provide a helper method to allocate the array on the native side, thus avoiding the copy.
     * Caller can use SuiteSparse_toc directly
     *
     * @param c SuiteSparse_config object
     * @return return from SuiteSparse_tic call
     */
    public static Pointer HelpTic(SuiteSparse_config c)
    {
        Pointer p = Memory.allocateDirect(Runtime.getSystemRuntime(), Double.BYTES * 2);
        c.SuiteSparse_tic(p);
        return p;
    }

    public static SuiteSparseVersion HelpVersion(SuiteSparse_config c)
    {
        int[] rv = new int[3];
        int w = c.SuiteSparse_version(rv);
        return new SuiteSparseVersion(w, rv[0], rv[1], rv[2]);
    }

}
