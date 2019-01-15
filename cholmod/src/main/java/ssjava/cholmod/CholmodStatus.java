package ssjava.cholmod;

public enum CholmodStatus
{
    CHOLMOD_OK(0),              /* success */
    CHOLMOD_NOT_INSTALLED(-1),  /* failure: method not installed */
    CHOLMOD_OUT_OF_MEMORY(-2),  /* failure: out of memory */
    CHOLMOD_TOO_LARGE(-3),      /* failure: integer overflow occured */
    CHOLMOD_INVALID(-4),        /* failure: invalid input */
    CHOLMOD_GPU_PROBLEM(-5),    /* failure: GPU fatal error */
    CHOLMOD_NOT_POSDEF(1),      /* warning: matrix not pos. def. */
    CHOLMOD_DSMALL(2);          /* warning: D for LDL'  or diag(L) or */

    private final int code;

    private CholmodStatus(int code)
    {
        this.code = code;
    }

    public int intValue()
    {
        return code;
    }
}
