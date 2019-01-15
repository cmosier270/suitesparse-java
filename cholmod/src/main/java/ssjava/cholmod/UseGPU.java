package ssjava.cholmod;

public enum UseGPU
{
    Prohibited(0),
    Requested(1),
    Undefined(-1);

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
