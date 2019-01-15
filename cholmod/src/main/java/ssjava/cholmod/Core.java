package ssjava.cholmod;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Struct;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.DoubleByReference;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.provider.FFIProvider;
import jnr.ffi.types.size_t;

/**
 * Maps functionality and definitions in choldmod_core.h
 */
public interface Core
{
    static Core Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<Core> ll = provider.createLibraryLoader(Core.class);
        return ll.load("cholmod");
    }

    static final int CHOLMOD_HOST_SUPERNODE_BUFFERS = 8;

    int cholmod_start(cholmod_common cc);
    int cholmod_l_start(cholmod_common cc);
    int cholmod_finish(cholmod_common cc);
    int cholmod_l_finish(cholmod_common cc);
    int cholmod_defaults(cholmod_common cc);
    int cholmod_l_defaults(cholmod_common cc);
    int cholmod_maxrank(@In @size_t int n, cholmod_common cc);
    int cholmod_l_maxrank(@In @size_t int n, cholmod_common cc);
    int cholmod_allocate_work(@In @size_t int nrow,
                              @In @size_t int iworksize,
                              @In @size_t int xworksize,
                              cholmod_common cc);
    int cholmod_l_allocate_work(@In @size_t int nrow,
                              @In @size_t int iworksize,
                              @In @size_t int xworksize,
                              cholmod_common cc);
    int cholmod_free_work(cholmod_common cc);
    int cholmod_l_free_work(cholmod_common cc);
    //TODO:  Handle SuiteSparse_long more flexibly
    long cholmod_clear_flag(cholmod_common cc);
    //TODO:  Handle SuiteSparse_long more flexibly
    long cholmod_l_clear_flag(cholmod_common cc);
    double cholmod_hypot(@In double x, @In double y);
    double cholmod_l_hypot(@In double x, @In double y);
    int cholmod_divcomplex(@In double ar, @In double ai,
                           @In double br, @In double bi,
                           @Out DoubleByReference cr,
                           @Out DoubleByReference ci);
    int cholmod_l_divcomplex(@In double ar, @In double ai,
                           @In double br, @In double bi,
                           @Out DoubleByReference cr,
                           @Out DoubleByReference ci);

    cholmod_sparse cholmod_allocate_sparse(@In @size_t int nrow,
                                           @In @size_t int ncol,
                                           @In @size_t int nzmax,
                                           @In int sorted,
                                           @In int packed,
                                           @In cholmod_sparse.SType stype,
                                           @In XType xtype,
                                           cholmod_common cc);

    cholmod_sparse cholmod_l_allocate_sparse(@In @size_t int nrow,
                                           @In @size_t int ncol,
                                           @In @size_t int nzmax,
                                           @In int sorted,
                                           @In int packed,
                                           @In cholmod_sparse.SType stype,
                                           @In XType xtype,
                                           cholmod_common cc);

    int cholmod_free_sparse(PointerByReference A, cholmod_common cc);
    int cholmod_l_free_sparse(PointerByReference A, cholmod_common cc);
    int cholmod_reallocate_sparse(@In @size_t int nznew, cholmod_sparse A, cholmod_common cc);
    int cholmod_l_reallocate_sparse(@In @size_t int nznew, cholmod_sparse A, cholmod_common cc);

    public static int Cholmod_Free_Sparse(Core core, cholmod_sparse A, cholmod_common cc)
    {
        return core.cholmod_free_sparse(new PointerByReference(Struct.getMemory(A)), cc);
    }
    public static int Cholmod_l_Free_Sparse(Core core, cholmod_sparse A, cholmod_common cc)
    {
        return core.cholmod_l_free_sparse(new PointerByReference(Struct.getMemory(A)), cc);
    }
}
