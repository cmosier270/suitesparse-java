package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_dense extends Struct implements MatrixRepresentation
{
    public final size_t nrow = new size_t();
    public final size_t ncol = new size_t();
    public final size_t nzmax = new size_t();
    public final size_t d = new size_t();
    public final Pointer x = new Pointer();
    public final Pointer z = new Pointer();
    public final Enum<XType> xtype = new Enum<>(XType.class);
    public final Enum<DType> dtype = new Enum<>(DType.class);

    public cholmod_dense(Runtime runtime)
    {
        super(runtime);
    }
    public cholmod_dense()
    {
        super(Runtime.getSystemRuntime());
    }

    @Override
    public MatrixValues<double[]> getDoubleValues()
    {
        int nz = nzmax.intValue();
        double[] Xz = null;
        switch (xtype.get())
        {
            case CHOLMOD_PATTERN:
                throw new RuntimeException("CHOLMOD_PATTERN unsupported");
            case CHOLMOD_ZOMPLEX:
            {
                Xz = new double[nz];
                z.get().get(0, Xz, 0, nz);
            }
            case CHOLMOD_COMPLEX:
                nz *= 2;
                break;
        }

        double[] Xx = new double[nz];
        x.get().get(0, Xx, 0, nz);

        return new MatrixValues<>(Xx, Xz);
    }

    @Override
    public void setValues(double[] Xx, double[] Xz)
    {
        int nv = Xx.length;
        switch (xtype.get())
        {
            case CHOLMOD_PATTERN:
                throw new RuntimeException("CHOLMOD_PATTERN unsupported");
            case CHOLMOD_ZOMPLEX:
                z.get().put(0, Xz, 0, nv);
                break;
        }

        x.get().put(0, Xx, 0, nv);
    }
}
