package ssjava.suitesparse_config;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.byref.DoubleByReference;
import jnr.ffi.byref.IntByReference;
import jnr.ffi.types.size_t;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SuiteSparse_configTest
{
    private final SuiteSparse_config ssc = SuiteSparse_config.Load();

    @Test
    void SuiteSparse_config()
    {
        ssc.SuiteSparse_start();
        Pointer p = ssc.SuiteSparse_malloc(1024, 4);
        p.putInt(1023, 25);
        IntByReference ibr = new IntByReference();
        Pointer p2 = ssc.SuiteSparse_realloc(2048, 1024, 4, p, ibr);
        assertEquals(1, ibr.intValue());
        assertEquals(25, p2.getInt(1023));
        assertEquals(null, ssc.SuiteSparse_free(p2));


        Pointer p3 = ssc.SuiteSparse_calloc(512, 8);
        p3.putDouble(511, 66.0);
        Pointer p4 = ssc.SuiteSparse_realloc(250000, 512, 8, p3, ibr);
        assertEquals(1, ibr.intValue());
        assertEquals(66.0, p4.getDouble(511));
        ssc.SuiteSparse_free(p4);

        ssc.SuiteSparse_finish();
    }

    @Test
    void time() throws InterruptedException
    {
        Pointer tic = SuiteSparse_config.HelpTic(ssc);
        Thread.sleep(2000);
        double diff = ssc.SuiteSparse_toc(tic);
        assertEquals(2.0, diff, 0.001);

        /**
         * TODO:  Determine an appropriate test value.
         * Couldn't determine what the number really was
         * without digging into more code.
         */
        double sst = ssc.SuiteSparse_time();
        System.out.format("SuiteSparse Time: %f\n", sst);
    }

    public static double[] testDivComplex(double ar, double ai, double br, double bi)
    {
        double den = br * br + bi * bi;
        double reans = (ar * br + ai * bi) / den;
        double imans = (ai * br - ar * bi) / den;
        return new double[] {reans, imans};
    }

    @Test
    void math()
    {
        assertEquals(Math.hypot(3.0, 4.0),
                ssc.SuiteSparse_hypot(3.0, 4.0));

        double re = 10.0;
        double im = 38.0;
        double divre = 2.0;
        double divim = 16;

        double[] ans = testDivComplex(re, im, divre, divim);

        DoubleByReference ssreans = new DoubleByReference();
        DoubleByReference ssimans = new DoubleByReference();
        assertEquals(0, ssc.SuiteSparse_divcomplex(re, im, divre, divim, ssreans, ssimans));
        assertEquals(ans[0], ssreans.doubleValue());
        assertEquals(ans[1], ssimans.doubleValue());
    }

    @Test
    public void version()
    {
        SuiteSparseVersion v = SuiteSparse_config.HelpVersion(ssc);
        /** Just test grabbing the value, not much else to do */
        System.out.format("Version: %s\n", v.toString());
    }

}