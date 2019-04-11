package ssjava.cholmod;

public class MatrixValues<T>
{
    private final T x;
    private final T z;

    public MatrixValues(T x, T z)
    {
        this.x = x;
        this.z = z;
    }

    public T getX()
    {
        return x;
    }

    public T getZ()
    {
        return z;
    }
}
