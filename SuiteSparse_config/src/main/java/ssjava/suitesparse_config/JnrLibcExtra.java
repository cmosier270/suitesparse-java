package ssjava.suitesparse_config;

import jnr.ffi.*;
import jnr.ffi.Runtime;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.provider.FFIProvider;
import jnr.ffi.types.*;

public interface JnrLibcExtra
{
    interface malloc_funcifc
    {
        @Delegate
        Pointer xmalloc(@size_t long size);
    }
    @FunctionalInterface
    interface calloc_func
    {
        @Delegate
        Pointer calloc(@Out @size_t long nmemb, @Out @size_t long size);
    }
    @FunctionalInterface
    interface realloc_func
    {
        @Delegate
        Pointer realloc(@Out Pointer ptr, @Out @size_t long size);
    }
    @FunctionalInterface
    interface free_func
    {
        @Delegate
        void free(@Out Pointer p);

    }
    public static JnrLibcExtra Load()
    {
        FFIProvider provider = FFIProvider.getSystemProvider();
        LibraryLoader<JnrLibcExtra> ll = provider.createLibraryLoader(JnrLibcExtra.class);
        return ll.load("c");
    }

    @int64_t Variable<Long> stderr();
    @int64_t Variable<Long> stdout();
    @int64_t Variable<Long> stdin();

    int fputs(@In String msg, @In Pointer stream);
    Pointer fgets(@In Pointer s, @In int size, Pointer stream);
    Pointer fopen(@In String pathname, @In String mode);
    int fclose(@In Pointer stream);
    int fflush(@In Pointer stream);

    public static Pointer StdioStream(Variable<Long> jnrstream)
    {
        return Pointer.wrap(Runtime.getSystemRuntime(), jnrstream.get());
    }

    Pointer memcpy(@In Pointer dest, @In Pointer src, @In @size_t long n);
}
