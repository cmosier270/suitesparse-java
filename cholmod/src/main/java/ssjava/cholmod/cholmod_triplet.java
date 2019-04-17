package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_triplet extends Struct implements MatrixRepresentation
{
    private static final java.lang.String INTERRI = "Requesting int indices but coded as %s";
    private static final java.lang.String INTERRL = "Requesting long indices but coded as %s";

    public enum SType
    {
        UNSYMMETRIC(0),
        SYMMETRIC_UPPER_TRANSPOSE_LOWER(1),
        SYMMETRIC_LOWER_TRANSPOSE_UPPER(-1);
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
    private final size_t nnz = new size_t();

    private final Pointer i = new Pointer();
    private final Pointer j = new Pointer();
    private final Pointer x = new Pointer();
    private final Pointer z = new Pointer();

    private final Enum<SType> stype = new Enum<>(SType.class);
    private final Enum<IType> itype = new Enum<>(IType.class);
    private final Enum<XType> xtype = new Enum<>(XType.class);
    private final Enum<DType> dtype = new Enum<>(DType.class);

    public cholmod_triplet(Runtime runtime)
    {
        super(runtime);
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

    public int getNNZ()
    {
        return nnz.intValue();
    }

    public void setNNZ(int nnz)
    {
        this.nnz.set(nnz);
    }

    public IType getIType()
    {
        return itype.get();
    }

    public jnr.ffi.Pointer getI()
    {
        return i.get();
    }

    public jnr.ffi.Pointer getJ()
    {
        return j.get();
    }
    public SType getSType()
    {
        return stype.get();
    }

    private int[] getIndicesAsInt(Pointer p, int n)
    {
        if (getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException(java.lang.String.format(
                    INTERRI, getIType()));
        }
        int[] rv = new int[n];
        p.get().get(0, rv, 0, n);
        return rv;
    }

    private void setIndices(int[] indices, Pointer p)
    {
        if (getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException(java.lang.String.format(
                    INTERRI, getIType()));
        }
        p.get().put(0, indices, 0, indices.length);
    }

    private long[] getIndicesAsLong(Pointer p, int n)
    {
        if (getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException(java.lang.String.format(
                    INTERRL, getIType()));
        }
        long[] rv = new long[n];
        p.get().get(0, rv, 0, n);
        return rv;
    }

    private void setIndices(long[] indices, Pointer p)
    {
        if (getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException(java.lang.String.format(
                    INTERRL, getIType()));
        }
        p.get().put(0, indices, 0, indices.length);
    }

    public int[] getRowIndicesAsInt()
    {
        return getIndicesAsInt(i, getNNZ());
    }

    public long[] getRowIndicesAsLong()
    {
        return getIndicesAsLong(i, getNNZ());
    }

    public void setRowIndices(int[] indices)
    {
        setIndices(indices, i);
    }

    public void setRowIndices(long[] indices)
    {
        setIndices(indices, i);
    }

    public int[] getColIndicesAsInt()
    {
        return getIndicesAsInt(j, getNNZ());
    }

    public long[] getColIndicesAsLong()
    {
        return getIndicesAsLong(j, getNNZ());
    }

    public void setColIndices(int[] indices)
    {
        setIndices(indices, j);
    }

    public void setColIndices(long[] indices)
    {
        setIndices(indices, j);
    }
}
