package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_dense extends Struct implements MatrixRepresentation
{
    private final size_t nrow = new size_t();
    private final size_t ncol = new size_t();
    private final size_t nzmax = new size_t();
    private final size_t d = new size_t();
    private final Pointer x = new Pointer();
    private final Pointer z = new Pointer();
    private final Enum<XType> xtype = new Enum<>(XType.class);
    private final Enum<DType> dtype = new Enum<>(DType.class);

    public cholmod_dense(Runtime runtime)
    {
        super(runtime);
    }
    public cholmod_dense()
    {
        super(Runtime.getSystemRuntime());
    }

    @Override
    public int getNRow()
    {
        return nrow.intValue();
    }

    @Override
    public int getNCol()
    {
        return ncol.intValue();
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
    public XType getXType()
    {
        return xtype.get();
    }

    @Override
    public int getNZMax()
    {
        return nzmax.intValue();
    }

    @Override
    public DType getDType()
    {
        return dtype.get();
    }

    public int getD()
    {
        return d.intValue();
    }
}
