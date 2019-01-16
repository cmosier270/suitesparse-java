package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_dense extends Struct
{
    public final size_t nrow = new size_t();
    public final size_t ncol = new size_t();
    public final size_t nzmax = new size_t();
    public final size_t d = new size_t();
    public final Pointer x = new Pointer();
    public final Pointer z = new Pointer();
    public final Enum<XType> xtype = new Enum<>(XType.class);
    public final Enum<DType> dtype = new Enum<>(DType.class);

    public cholmod_dense(Runtime runtime)
    {
        super(runtime);
    }
}
