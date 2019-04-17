package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_factor extends Struct implements CompressedColumn, MatrixRepresentation
{
    private final size_t n = new size_t();
    private final size_t minor = new size_t();
    private final Pointer Perm = new Pointer();
    private final Pointer ColCount = new Pointer();
    private final Pointer IPerm = new Pointer();
    private final size_t nzmax = new size_t();
    private final Pointer p = new Pointer();
    private final Pointer i = new Pointer();
    private final Pointer x = new Pointer();
    private final Pointer z = new Pointer();
    private final Pointer nz = new Pointer();
    private final Pointer next = new Pointer();
    private final Pointer prev = new Pointer();
    private final size_t nsuper = new size_t();
    private final size_t ssize = new size_t();
    private final size_t xsize = new size_t();
    private final size_t maxcsize = new size_t();
    private final size_t maxesize = new size_t();
    private final Pointer psuper = new Pointer();
    private final Pointer pi = new Pointer();
    private final Pointer px = new Pointer();
    private final Pointer s = new Pointer();
    private final Enum<Order> ordering = new Enum<>(Order.class);
    private final int32_t is_ll = new int32_t();
    private final int32_t is_super = new int32_t();
    private final int32_t is_monotonic = new int32_t();
    private final Enum<IType> itype = new Enum<>(IType.class);
    private final Enum<XType> xtype = new Enum<>(XType.class);
    private final Enum<DType> dtype = new Enum<>(DType.class);
    private final Enum<UseGPU> useGPU = new Enum<>(UseGPU.class);
    public cholmod_factor(Runtime runtime)
    {
        super(runtime);
    }

    public int getN()
    {
        return n.intValue();
    }

    @Override
    public int getNRow()
    {
        return getN();
    }

    @Override
    public int getNCol()
    {
        return getN();
    }

    public long getMinor()
    {
        return minor.longValue();
    }

    public jnr.ffi.Pointer getPerm()
    {
        return Perm.get();
    }

    public jnr.ffi.Pointer getColCount()
    {
        return ColCount.get();
    }

    public jnr.ffi.Pointer getIPerm()
    {
        return IPerm.get();
    }

    @Override
    public int getNZMax()
    {
        return nzmax.intValue();
    }

    @Override
    public jnr.ffi.Pointer getP()
    {
        return p.get();
    }

    @Override
    public jnr.ffi.Pointer getI()
    {
        return i.get();
    }

    @Override
    public jnr.ffi.Pointer getX()
    {
        return x.get();
    }

    @Override
    public jnr.ffi.Pointer getZ()
    {
        return z.get();
    }

    @Override
    public jnr.ffi.Pointer getNZ()
    {
        return nz.get();
    }


    public jnr.ffi.Pointer getNext()
    {
        return next.get();
    }

    public jnr.ffi.Pointer getPrev()
    {
        return prev.get();
    }

    public int getNSuper()
    {
        return nsuper.intValue();
    }

    public int getSSize()
    {
        return ssize.intValue();
    }

    public int getXSize()
    {
        return xsize.intValue();
    }

    public long getMaxcsize()
    {
        return maxcsize.longValue();
    }

    public long getMaxesize()
    {
        return maxesize.longValue();
    }

    public jnr.ffi.Pointer getSuper()
    {
        return psuper.get();
    }

    public jnr.ffi.Pointer getPi()
    {
        return pi.get();
    }

    public jnr.ffi.Pointer getPx()
    {
        return px.get();
    }

    public jnr.ffi.Pointer getS()
    {
        return s.get();
    }

    public Order getOrdering()
    {
        return ordering.get();
    }

    public boolean isLL()
    {
        return is_ll.intValue() != 0;
    }

    public boolean isSuper()
    {
        return is_super.intValue() != 0;
    }

    public boolean isMonotonic()
    {
        return is_monotonic.intValue() != 0;
    }

    public IType getIType()
    {
        return itype.get();
    }

    @Override
    public XType getXType()
    {
        return xtype.get();
    }

    @Override
    public DType getDType()
    {
        return dtype.get();
    }

    public UseGPU getUseGPU()
    {
        return useGPU.get();
    }

    public boolean isSorted() {return true;}

    public int[] getColumnNonZeros()
    {
        int n = getNCol();
        int[] rv = new int[n];
        getNZ().get(0, rv, 0, n);
        return rv;
    }
}
