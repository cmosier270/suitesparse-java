package ssjava.cholmod;

public interface MatrixRepresentation
{
    MatrixValues<double[]> getDoubleValues();
    default MatrixValues<float[]> getFloatValues()
    {
        throw new UnsupportedOperationException("CHOLMOD_SINGLE is not supported");
    }
    default void setValues(MatrixValues<double[]> values)
    {
        setValues(values.getX(), values.getZ());
    }
    void setValues(double[] x, double[] z);
}
