package ssjava.suitesparse_config;

public class SuiteSparseVersion
{
    private final int suitesparseVersion;
    private final int suitesparseMainVersion;
    private final int suitesparseSubVersion;
    private final int suitesparseSubsubVersion;

    public SuiteSparseVersion(int suitesparseVersion, int suitesparseMainVersion, int suitesparseSubVersion, int suitesparseSubsubVersion)
    {
        this.suitesparseVersion = suitesparseVersion;
        this.suitesparseMainVersion = suitesparseMainVersion;
        this.suitesparseSubVersion = suitesparseSubVersion;
        this.suitesparseSubsubVersion = suitesparseSubsubVersion;
    }

    public int getSuitesparseMainVersion()
    {
        return suitesparseMainVersion;
    }

    public int getSuitesparseSubVersion()
    {
        return suitesparseSubVersion;
    }

    public int getSuitesparseSubsubVersion()
    {
        return suitesparseSubsubVersion;
    }

    public int getSuitesparseVersion()
    {
        return suitesparseVersion;
    }

    @Override
    public String toString()
    {
        return String.format("[%d,%d.%d.%d]", suitesparseVersion,
                suitesparseMainVersion,
                suitesparseSubVersion,
                suitesparseSubsubVersion);
    }
}
