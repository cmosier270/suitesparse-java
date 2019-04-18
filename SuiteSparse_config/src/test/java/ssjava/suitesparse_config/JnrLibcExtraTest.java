package ssjava.suitesparse_config;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JnrLibcExtraTest
{

    private final JnrLibcExtra libc = JnrLibcExtra.Load();
    @Test
    void stdout_global_var()
    {
        Pointer stdout = JnrLibcExtra.StdioStream(libc.stdout());
        assertTrue(libc.fputs("Writing through LIBC", stdout) > 0);
    }

    @Test
    void stdioopenwrite() throws IOException
    {
        final String teststr = UUID.randomUUID().toString();
        File tmp = File.createTempFile("jnrlibc", null);

        Pointer fp = libc.fopen(tmp.getAbsolutePath(), "w");
        assertNotEquals(null, fp);
        assertTrue(libc.fputs(teststr,fp)>0);
        assertTrue(libc.fputs("\n",fp)>0);
        assertEquals(0, libc.fclose(fp));

        BufferedReader rdr = new BufferedReader(new FileReader(tmp));
        String s = rdr.readLine().trim();
        assertEquals(teststr, s);

        tmp.delete();
    }

    @Test void stdioOpenRead() throws IOException
    {
        final String teststr = UUID.randomUUID().toString();
        File tmp = File.createTempFile("jnrlibc", null);

        PrintWriter wrtr = new PrintWriter(new FileWriter(tmp));
        wrtr.println(teststr);
        wrtr.close();

        Pointer fp = libc.fopen(tmp.getAbsolutePath(), "r");
        assertNotEquals(null, fp);
        Pointer buf = Memory.allocateDirect(Runtime.getSystemRuntime(), 4096);
        assertNotEquals(null, libc.fgets(buf, 4096, fp));
        assertEquals(teststr, buf.getString(0).trim());

        tmp.delete();
    }

    @Test void memcpy_test()
    {
        int n = 25;
        Runtime r = Runtime.getSystemRuntime();
        Pointer p = Memory.allocateDirect(r, n*Integer.BYTES);
        int[] vals = new int[n];
        for(int i=0; i < n; ++i)
        {
            vals[i] = i+1;
        }
        p.put(0, vals, 0, n);

        Pointer p2 = Memory.allocateDirect(r, n * Integer.BYTES);
        libc.memcpy(p2, p, n * Integer.BYTES);
        int[] vals2 = new int[n];;
        p2.get(0, vals2, 0, n);
        assertArrayEquals(vals, vals2);

    }

}