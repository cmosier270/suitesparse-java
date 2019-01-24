package ssjava.cholmod;

public enum SolveSystem
{
    CHOLMOD_A(0),
    CHOLMOD_LDLt(1),
    CHOLMOD_LD(2),
    CHOLMOD_DLt(3),
    CHOLMOD_L(4),
    CHOLMOD_Lt(5),
    CHOLMOD_D(6),
    CHOLMOD_P(7),
    CHOLMOD_Pt(8);

    private int code;
    SolveSystem(int code) {this.code = code;}
    public int getCode() {return code;}


}
