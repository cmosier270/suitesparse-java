package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class CholmodMethod extends Struct
{
    public final Double lnz = new Double();
    public final Double fl = new Double();
    public final Double prune_dense = new Double();
    public final Double prune_dense2 = new Double();
    public final Double nd_oksep = new Double();
    public final Double[] other_1 = array(new Double[4]);
    public final size_t nd_small = new size_t();
    public final size_t[] other_2 = array(new size_t[4]);
    public final int32_t aggressive = new int32_t();
    public final int32_t order_for_lu = new int32_t();
    public final int32_t nd_compress = new int32_t();
    public final int32_t nd_camd = new int32_t();
    public final int32_t nd_components = new int32_t();
    public final Enum<ssjava.cholmod.Order> ordering = new Enum<>(ssjava.cholmod.Order.class);
    public final size_t[] other_3 = array(new size_t[4]);

    public CholmodMethod(Runtime runtime)
    {
        super(runtime);
    }
}
