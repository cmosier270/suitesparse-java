package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_triplet extends Struct
{
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
    public final size_t nrow = new size_t();
    public final size_t ncol = new size_t();
    public final size_t nzmax = new size_t();
    public final size_t nnz = new size_t();

    public final Pointer i = new Pointer();
    public final Pointer j = new Pointer();
    public final Pointer x = new Pointer();
    public final Pointer z = new Pointer();

    public final Enum<SType> stype = new Enum<>(SType.class);
    public final Enum<IType> itype = new Enum<>(IType.class);
    public final Enum<XType> xtype = new Enum<>(XType.class);
    public final Enum<DType> dtype = new Enum<>(DType.class);

    public cholmod_triplet(Runtime runtime)
    {
        super(runtime);
    }

    public cholmod_triplet()
    {
        this(Runtime.getSystemRuntime());
    }
}
