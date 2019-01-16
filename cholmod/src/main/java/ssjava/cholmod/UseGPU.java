package ssjava.cholmod;

public enum UseGPU
{
    PROHIBITED(0),
    REQUESTED(1),
    UNDEFINED(-1);

    private final int code;

    private UseGPU(int code)
    {
        this.code = code;
    }

    public int intValue()
    {
        return code;
    }

}
