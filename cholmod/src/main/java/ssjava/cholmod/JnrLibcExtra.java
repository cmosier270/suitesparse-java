package ssjava.cholmod;

import jnr.ffi.*;
import jnr.ffi.Runtime;
import jnr.ffi.annotations.In;
import jnr.ffi.provider.FFIProvider;
import jnr.ffi.types.*;

public interface JnrLibcExtra
{
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
}
