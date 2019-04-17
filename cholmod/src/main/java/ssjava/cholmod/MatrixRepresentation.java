package ssjava.cholmod;

import jnr.ffi.Pointer;

/**
 * CHOLMOD has a lot of flexibility in data format.  Specifically,
 * complex matrix values can be stored in two separate ways:
 * CHOLMOD_COMPLEX, and CHOLMOD_ZOMPLEX.
 *
 * This interface provides methods to expose the data in either format
 * to help the application.  The application still
 * requires knowledge of the data format to be able to use it correctly.
 */
public interface MatrixRepresentation
{
    Pointer getX();
    Pointer getZ();
    XType getXType();
    int getNZMax();
    int getNRow();
    int getNCol();
    DType getDType();
    default MatrixValues<float[]> getFloatValues()
    {
        throw new UnsupportedOperationException("CHOLMOD_SINGLE is not supported");
    }
    default void setValues(MatrixValues<double[]> values)
    {
        setValues(values.getX(), values.getZ());
    }
    default MatrixValues<double[]> getDoubleValues()
    {
        int nz = getNZMax();
        double[] Xz = null;
        switch (getXType())
        {
            case CHOLMOD_PATTERN:
                throw new UnsupportedOperationException("CHOLMOD_PATTERN unsupported");
            case CHOLMOD_ZOMPLEX:
            {
                Xz = new double[nz];
                getZ().get(0, Xz, 0, nz);
                break;
            }
            case CHOLMOD_COMPLEX:
                nz *= 2;
                break;
        }

        double[] Xx = new double[nz];
        getX().get(0, Xx, 0, nz);

        return new MatrixValues<>(Xx, Xz);
    }

    default void setValues(double[] Xx, double[] Xz)
    {
        int nv = Xx.length;
        switch (getXType())
        {
            case CHOLMOD_PATTERN:
                throw new UnsupportedOperationException("CHOLMOD_PATTERN unsupported");
            case CHOLMOD_ZOMPLEX:
                getZ().put(0, Xz, 0, nv);
                break;
        }

        getX().put(0, Xx, 0, nv);
    }
}
