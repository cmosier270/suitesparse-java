package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class CholmodMethod extends Struct
{
    private final Double lnz = new Double();
    private final Double fl = new Double();
    private final Double prune_dense = new Double();
    private final Double prune_dense2 = new Double();
    private final Double nd_oksep = new Double();
    private final Double[] other_1 = array(new Double[4]);
    private final size_t nd_small = new size_t();
    private final size_t[] other_2 = array(new size_t[4]);
    private final int32_t aggressive = new int32_t();
    private final int32_t order_for_lu = new int32_t();
    private final int32_t nd_compress = new int32_t();
    private final int32_t nd_camd = new int32_t();
    private final int32_t nd_components = new int32_t();
    private final Enum<ssjava.cholmod.Order> ordering = new Enum<>(ssjava.cholmod.Order.class);
    private final size_t[] other_3 = array(new size_t[4]);

    public CholmodMethod(Runtime runtime)
    {
        super(runtime);
    }

    public double getLnz()
    {
        return lnz.doubleValue();
    }

    public void setLnz(double lnz)
    {
        this.lnz.set(lnz);
    }

    public double getFl()
    {
        return fl.doubleValue();
    }

    public void setFl(double fl)
    {
        this.fl.set(fl);
    }

    public double getPrune_dense()
    {
        return prune_dense.doubleValue();
    }

    public void setPrune_dense(double value)
    {
        prune_dense.set(value);
    }

    public double getPrune_dense2()
    {
        return prune_dense2.doubleValue();
    }

    public void setPrune_dense2(double value)
    {
        prune_dense2.set(value);
    }

    public double getNd_oksep()
    {
        return nd_oksep.doubleValue();
    }

    public void setNd_oksep(double nd_oksep)
    {
        this.nd_oksep.set(nd_oksep);
    }

    public Double[] getOther_1()
    {
        return other_1;
    }

    public double getOther_1(int index)
    {
        return other_1[index].doubleValue();
    }

    public void setOther_1(int index, double other_1)
    {
        this.other_1[index].set(other_1);
    }

    public int getNd_small()
    {
        return nd_small.intValue();
    }

    public void setNd_small(int nd_small)
    {
        this.nd_small.set(nd_small);
    }

    public size_t[] getOther_2()
    {
        return other_2;
    }

    public long getOther_2(int index)
    {
        return other_2[index].longValue();
    }

    public void setOther_2(int index, long value)
    {
        other_2[index].set(value);
    }

    public boolean isAggressive()
    {
        return aggressive.intValue() != 0;
    }

    public void setAggressive(boolean aggressive)
    {
        this.aggressive.set(aggressive ? 1 : 0);
    }

    public boolean performOrder_for_lu()
    {
        return this.order_for_lu.intValue() != 0;
    }

    public void setOrder_for_lu(boolean order_for_lu)
    {
        this.order_for_lu.set(order_for_lu ? 1 : 0);
    }

    public boolean performNd_compress()
    {
        return nd_compress.intValue() != 0;
    }

    public void setNd_compress(boolean nd_compress)
    {
        this.nd_compress.set(nd_compress?1:0);
    }

    public boolean getNd_camd()
    {
        return nd_camd.intValue() != 0;
    }

    public void setNd_camd(boolean nd_camd)
    {
        this.nd_camd.set(nd_camd ? 1 : 0);
    }

    public boolean getNd_components()
    {
        return nd_components.intValue() != 0;
    }

    public void setNd_components(boolean nd_components)
    {
        this.nd_components.set(nd_components ? 1 : 0);
    }

    public Order getOrdering()
    {
        return ordering.get();
    }

    public void setOrdering(Order ordering)
    {
        this.ordering.set(ordering);
    }

    public size_t[] getOther_3()
    {
        return other_3;
    }

    public long getOther_3(int index)
    {
        return other_3[index].longValue();
    }

    public void setOther_3(int index, long value)
    {
        other_3[index].set(value);
    }
}
