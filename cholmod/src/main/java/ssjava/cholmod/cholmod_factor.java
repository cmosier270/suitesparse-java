package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_factor extends Struct
{
    public final size_t n = new size_t();
    public final size_t minor = new size_t();
    public final Pointer Perm = new Pointer();
    public final Pointer ColCount = new Pointer();
    public final Pointer IPerm = new Pointer();
    public final size_t nzmax = new size_t();
    public final Pointer p = new Pointer();
    public final Pointer i = new Pointer();
    public final Pointer x = new Pointer();
    public final Pointer z = new Pointer();
    public final Pointer nz = new Pointer();
    public final Pointer next = new Pointer();
    public final Pointer prev = new Pointer();
    public final size_t nsuper = new size_t();
    public final size_t ssize = new size_t();
    public final size_t xsize = new size_t();
    public final size_t maxcsize = new size_t();
    public final size_t maxesize = new size_t();
    public final Pointer psuper = new Pointer();
    public final Pointer pi = new Pointer();
    public final Pointer px = new Pointer();
    public final Pointer s = new Pointer();
    public final Enum<Order> ordering = new Enum<>(Order.class);
    public final int32_t is_ll = new int32_t();
    public final int32_t is_super = new int32_t();
    public final int32_t is_monotonic = new int32_t();
    public final Enum<IType> itype = new Enum<>(IType.class);
    public final Enum<XType> xtype = new Enum<>(XType.class);
    public final Enum<DType> dtype = new Enum<>(DType.class);
    public final Enum<UseGPU> useGPU = new Enum<>(UseGPU.class);
    public cholmod_factor(Runtime runtime)
    {
        super(runtime);
    }
    public cholmod_factor()
    {
        this(Runtime.getSystemRuntime());
    }
}
