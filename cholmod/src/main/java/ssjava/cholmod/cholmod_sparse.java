package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_sparse extends Struct implements MatrixRepresentation, CompressedColumn
{
    public enum SType
    {
        UNSYMMETRIC(0),
        SYMMETRIC_UPPER(1),
        SYMMETRIC_LOWER(-1);
        private final int code;

        SType(int code)
        {
            this.code = code;
        }

        public int intValue()
        {
            return code;
        }
    }

    private final size_t nrow = new size_t();
    private final size_t ncol = new size_t();
    private final size_t nzmax = new size_t();

    private final Pointer p = new Pointer();
    private final Pointer i = new Pointer();
    private final Pointer nz = new Pointer();
    private final Pointer x = new Pointer();
    private final Pointer z = new Pointer();

    private final Enum<SType> stype = new Enum<>(SType.class);
    private final Enum<ssjava.cholmod.IType> itype = new Enum<>(ssjava.cholmod.IType.class);
    private final Enum<ssjava.cholmod.XType> xtype = new Enum<>(ssjava.cholmod.XType.class);
    private final Enum<ssjava.cholmod.DType> dtype = new Enum<>(ssjava.cholmod.DType.class);

    private final int32_t sorted = new int32_t();
    private final int32_t packed = new int32_t();

    public cholmod_sparse(Runtime runtime)
    {
        super(runtime);
    }
    public cholmod_sparse()
    {
        this(Runtime.getSystemRuntime());
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
    public DType getDType()
    {
        return dtype.get();
    }

    public SType getSType()
    {
        return stype.get();
    }

    @Override
    public IType getIType()
    {
        return itype.get();
    }

    @Override
    public boolean isSorted()
    {
        return sorted.intValue() != 0;
    }

    public boolean isPacked()
    {
        return packed.intValue() != 0;
    }

    @Override
    public jnr.ffi.Pointer getNZ()
    {
        return nz.get();
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

}
