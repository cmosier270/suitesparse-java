package ssjava.cholmod;

import jnr.ffi.Pointer;

public interface CompressedColumn
{
    Pointer getP();
    Pointer getI();
    Pointer getNZ();
    boolean isSorted();
    IType getIType();
    int getNCol();
    int getNZMax();

    default int[] getColumnPointersAsInt()
    {
        if(getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException("Column Pointers not configured as CHOLMOD_INT");
        }
        int n = getNCol()+1;
        int[] rv = new int[n];
        getP().get(0, rv, 0, n);
        return rv;
    }

    default long[] getColumnPointersAsLong()
    {
        if(getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException("Column Pointers not configured as CHOLMOD_LONG");
        }
        int n = getNCol()+1;
        long[] rv = new long[n];
        getP().get(0, rv, 0, n);
        return rv;
    }

    default void setColumnPointers(int[] p)
    {
        if(getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException("Column Pointers not configured as CHOLMOD_INT");
        }
        int n = p.length;
        getP().put(0, p, 0, n);
    }
    default void setColumnPointers(long[] p)
    {
        if(getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException("Column Pointers not configured as CHOLMOD_LONG");
        }
        int n = p.length;
        getP().put(0, p, 0, n);
    }

    default int[] getRowIndicesAsInt()
    {
        if(getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException("Row Indices not configured as CHOLMOD_INT");
        }
        int n = getNZMax();
        int[] rv = new int[n];
        getI().get(0, rv, 0, n);
        return rv;
    }

    default long[] getRowIndicesAsLong()
    {
        if(getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException("Row Indices not configured as CHOLMOD_LONG");
        }
        int n = getNZMax();
        long[] rv = new long[n];
        getI().get(0, rv, 0, n);
        return rv;
    }

    default void setRowIndices(int[] i)
    {
        if(getIType() != IType.CHOLMOD_INT)
        {
            throw new UnsupportedOperationException("Row Indices not configured as CHOLMOD_INT");
        }
        int n = i.length;
        getI().put(0, i, 0, n);
    }

    default void setRowIndices(long[] i)
    {
        if(getIType() != IType.CHOLMOD_LONG)
        {
            throw new UnsupportedOperationException("Row Indices not configured as CHOLMOD_LONG");
        }
        int n = i.length;
        getI().put(0, i, 0, n);
    }

}
