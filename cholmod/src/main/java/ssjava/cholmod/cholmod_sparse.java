package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_sparse extends Struct
{
    public enum SType
    {
        Unsymmetric(0),
        SymmetricUpper(1),
        SymmetricLower(-1);
        private final int code;

        private SType(int code)
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

    public final Pointer p = new Pointer();
    public final Pointer i = new Pointer();
    public final Pointer nz = new Pointer();
    public final Pointer x = new Pointer();
    public final Pointer z = new Pointer();

    public final Enum<SType> stype = new Enum<>(SType.class);
    public final Enum<ssjava.cholmod.IType> itype = new Enum<>(ssjava.cholmod.IType.class);
    public final Enum<ssjava.cholmod.XType> xtype = new Enum<>(ssjava.cholmod.XType.class);
    public final Enum<ssjava.cholmod.DType> dtype = new Enum<>(ssjava.cholmod.DType.class);

    public final int32_t sorted = new int32_t();
    public final int32_t packed = new int32_t();
    public cholmod_sparse(Runtime runtime)
    {
        super(runtime);
    }
}
