package ssjava.cholmod;

/**
 * Return Matrix data either in a single array.  For complex values
 * when CHOLMOD_COMPLEX is used, the imaginary part follows the real
 * part, so the array X is 2 * nzmax.  Z is null.
 *
 * For CHOLMOD_ZOMPLEX, X and Z are parallel arrays, with X the real part, and
 * Z the imaginary part.
 *
 * @param <T> either float[] or double[], althoguth float[] is not
 *           supported yet in the base CHOLMOD library.
 */
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
