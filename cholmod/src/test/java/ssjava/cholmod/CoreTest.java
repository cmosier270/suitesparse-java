package ssjava.cholmod;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.Struct.int32_t;
import jnr.ffi.byref.DoubleByReference;
import jnr.ffi.byref.PointerByReference;
import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;

import static org.junit.jupiter.api.Assertions.*;

class CoreTest
{
    private final Core core = Core.Load();

    private double[] convertArray(Struct.Double[] da)
    {
        int n = da.length;
        double[] rv = new double[n];
        for (int i = 0; i < n; ++i)
        {
            rv[i] = da[i].doubleValue();
        }
        return rv;
    }

    private long[] convertArray(Struct.size_t[] la)
    {
        int n = la.length;
        long[] rv = new long[n];
        for (int i = 0; i < n; ++i)
        {
            rv[i] = la[i].longValue();
        }
        return rv;
    }

    private long[] convertArray(Struct.int64_t[] ia)
    {
        int n = ia.length;
        long[] rv = new long[n];
        for (int i = 0; i < n; ++i)
        {
            rv[i] = ia[i].longValue();
        }
        return rv;
    }


    private void checkDefaultCommon(cholmod_common cc, IType iType, UseGPU defugpu)
    {
        /**
         * This method is automatically generated by [ins link].  The values
         * present are taken from the structure immediately after
         * cholmod_start is called
         *
         * TODO:  skipped the error callback here, belongs elsewhere
         * TODO:  skipped method inner structure.
         * TODO:  skipped gpuStream, cublasEventPotrf, and updateCBuffersFree
         * TODO:  skipped devBuffSize, hardware dependent
         * TODO:  skipping ibuffer, value does not appear to be set as part of cholmod_start
         */

        assertEquals(0.000000, cc.dbound.doubleValue());
        assertEquals(1.200000, cc.grow0.doubleValue());
        assertEquals(1.200000, cc.grow1.doubleValue());
        assertEquals(5, cc.grow2.longValue());
        assertEquals(8, cc.maxrank.longValue());
        assertEquals(40.000000, cc.supernodal_switch.doubleValue());
        assertEquals(1, cc.supernodal.intValue());
        assertEquals(1, cc.final_asis.intValue());
        assertEquals(1, cc.final_super.intValue());
        assertEquals(0, cc.final_ll.intValue());
        assertEquals(1, cc.final_pack.intValue());
        assertEquals(1, cc.final_monotonic.intValue());
        assertEquals(0, cc.final_resymbol.intValue());
        assertArrayEquals(new double[]{0.8, 0.1, 0.05}, convertArray(cc.zrelax));
        assertArrayEquals(new long[]{4, 16, 48}, convertArray(cc.nrelax));
        assertEquals(0, cc.prefer_zomplex.intValue());
        assertEquals(1, cc.prefer_upper.intValue());
        assertEquals(0, cc.quick_return_if_not_posdef.intValue());
        assertEquals(0, cc.prefer_binary.intValue());
        assertEquals(3, cc.print.intValue());
        assertEquals(0, cc.precise.intValue());
        assertEquals(0, cc.try_catch.intValue());
        assertEquals(0, cc.nmethods.intValue());
        assertEquals(0, cc.current.intValue());
        assertEquals(0, cc.selected.intValue());
        assertEquals(1, cc.postorder.intValue());
        assertEquals(0, cc.default_nesdis.intValue());
        assertEquals(0.000000, cc.metis_memory.doubleValue());
        assertEquals(0.660000, cc.metis_dswitch.doubleValue());
        assertEquals(3000, cc.metis_nswitch.longValue());
        assertEquals(0, cc.nrow.longValue());
        assertEquals(-1, cc.mark.longValue());
        assertEquals(0, cc.iworksize.longValue());
        assertEquals(0, cc.xworksize.longValue());
        assertEquals(null, cc.Flag.get());
        assertEquals(null, cc.Head.get());
        assertEquals(null, cc.Xwork.get());
        assertEquals(null, cc.Iwork.get());
        assertEquals(iType, cc.itype.get());
        assertEquals(0, cc.dtype.intValue());
        assertEquals(0, cc.no_workspace_reallocate.intValue());
        assertEquals(CholmodStatus.CHOLMOD_OK, cc.status.get());
        assertEquals(-1.000000, cc.fl.doubleValue());
        assertEquals(-1.000000, cc.lnz.doubleValue());
        assertEquals(0.000000, cc.anz.doubleValue());
        assertEquals(-1.000000, cc.modfl.doubleValue());
        assertEquals(0, cc.malloc_count.longValue());
        assertEquals(0, cc.memory_usage.longValue());
        assertEquals(0, cc.memory_inuse.longValue());
        assertEquals(0.000000, cc.nrealloc_col.doubleValue());
        assertEquals(0.000000, cc.nrealloc_factor.doubleValue());
        assertEquals(0.000000, cc.ndbounds_hit.doubleValue());
        assertEquals(0.000000, cc.rowfacfl.doubleValue());
        assertEquals(-1.000000, cc.aatfl.doubleValue());
        assertEquals(0, cc.called_nd.intValue());
        assertEquals(1, cc.blas_ok.intValue());
        assertEquals(1.000000, cc.SPQR_grain.doubleValue());
        assertEquals(1000000.000000, cc.SPQR_small.doubleValue());
        assertEquals(1, cc.SPQR_shrink.intValue());
        assertEquals(0, cc.SPQR_nthreads.intValue());
        assertEquals(0.000000, cc.SPQR_flopcount.doubleValue());
        assertEquals(0.000000, cc.SPQR_analyze_time.doubleValue());
        assertEquals(0.000000, cc.SPQR_factorize_time.doubleValue());
        assertEquals(0.000000, cc.SPQR_solve_time.doubleValue());
        assertEquals(0.000000, cc.SPQR_flopcount_bound.doubleValue());
        assertEquals(0.000000, cc.SPQR_tol_used.doubleValue());
        assertEquals(0.000000, cc.SPQR_norm_E_fro.doubleValue());
        assertArrayEquals(new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, convertArray(cc.SPQR_istat));
        assertEquals(defugpu, cc.useGPU.get());
        assertEquals(0, cc.maxGpuMemBytes.longValue());
        assertEquals(0.000000, cc.maxGpuMemFraction.doubleValue());
        assertEquals(1, cc.gpuMemorySize.longValue());
        assertEquals(0.000000, cc.gpuKernelTime.doubleValue());
        assertEquals(0, cc.gpuFlops.longValue());
        assertEquals(0, cc.gpuNumKernelLaunches.intValue());
        assertEquals(null, cc.cublasHandle.get());
        assertEquals(null, cc.updateCKernelsComplete.get());
        assertEquals(null, cc.dev_mempool.get());
        assertEquals(0, cc.dev_mempool_size.longValue());
        assertEquals(null, cc.host_pinned_mempool.get());
        assertEquals(0, cc.host_pinned_mempool_size.longValue());
        assertEquals(0.000000, cc.cholmod_cpu_gemm_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_cpu_syrk_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_cpu_trsm_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_cpu_potrf_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_gpu_gemm_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_gpu_syrk_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_gpu_trsm_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_gpu_potrf_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_assemble_time.doubleValue());
        assertEquals(0.000000, cc.cholmod_assemble_time2.doubleValue());
        assertEquals(0, cc.cholmod_cpu_gemm_calls.longValue());
        assertEquals(0, cc.cholmod_cpu_syrk_calls.longValue());
        assertEquals(0, cc.cholmod_cpu_trsm_calls.longValue());
        assertEquals(0, cc.cholmod_cpu_potrf_calls.longValue());
        assertEquals(0, cc.cholmod_gpu_gemm_calls.longValue());
        assertEquals(0, cc.cholmod_gpu_syrk_calls.longValue());
        assertEquals(0, cc.cholmod_gpu_trsm_calls.longValue());
        assertEquals(0, cc.cholmod_gpu_potrf_calls.longValue());
    }

    @Test
    public void cholmod_common_struct()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        checkDefaultCommon(cc, IType.CHOLMOD_INT, UseGPU.Prohibited);
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_common_struct()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        checkDefaultCommon(cc, IType.CHOLMOD_LONG, UseGPU.Undefined);
        assertEquals(1, core.cholmod_l_finish(cc));
    }

    @Test
    public void cholmod_defaults()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        cc.useGPU.set(UseGPU.Requested);
        assertEquals(UseGPU.Requested, cc.useGPU.get());
        core.cholmod_defaults(cc);
        checkDefaultCommon(cc, IType.CHOLMOD_INT, UseGPU.Prohibited);
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_defaults()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        cc.useGPU.set(UseGPU.Requested);
        assertEquals(UseGPU.Requested, cc.useGPU.get());
        core.cholmod_l_defaults(cc);
        checkDefaultCommon(cc, IType.CHOLMOD_LONG, UseGPU.Undefined);
        assertEquals(1, core.cholmod_l_finish(cc));
    }

    @Test
    public void cholmod_maxrank()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        cc.maxrank.set(1);
        assertEquals(2, core.cholmod_maxrank(1, cc));
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_maxrank()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        cc.maxrank.set(1);
        assertEquals(2, core.cholmod_l_maxrank(1, cc));
        assertEquals(1, core.cholmod_l_finish(cc));
    }

    @Test
    public void cholmod_allocate_work()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        assertEquals(null, cc.Iwork.get());
        assertEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_allocate_work(1500, 4, 8, cc));
        assertNotEquals(null, cc.Iwork.get());
        assertNotEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_free_work(cc));
        assertEquals(null, cc.Iwork.get());
        assertEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_allocate_work()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        assertEquals(null, cc.Iwork.get());
        assertEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_l_allocate_work(1500, 4, 8, cc));
        assertNotEquals(null, cc.Iwork.get());
        assertNotEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_l_free_work(cc));
        assertEquals(null, cc.Iwork.get());
        assertEquals(null, cc.Xwork.get());
        assertEquals(1, core.cholmod_l_finish(cc));
    }

    @Test
    public void cholmod_clear_flag()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        assertEquals(-1, cc.mark.intValue());
        assertEquals(0, core.cholmod_clear_flag(cc));
        assertEquals(0, cc.mark.intValue());
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_clear_flag()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        assertEquals(-1, cc.mark.intValue());
        assertEquals(0, core.cholmod_l_clear_flag(cc));
        assertEquals(0, cc.mark.get());
        assertEquals(1, core.cholmod_l_finish(cc));
    }

    @Test
    public void cholmod_hypot()
    {
        assertEquals(Math.hypot(3.0, 4.0),
                core.cholmod_hypot(3.0, 4.0));

    }

    @Test
    public void cholmod_l_hypot()
    {
        assertEquals(Math.hypot(3.0, 4.0),
                core.cholmod_l_hypot(3.0, 4.0));

    }

    @Test
    public void cholmod_divcomplex()
    {
        double re = 10.0;
        double im = 38.0;
        double divre = 2.0;
        double divim = 16;

        double den = divre * divre + divim * divim;
        double reans = (re * divre + im * divim) / den;
        double imans = (im * divre - re * divim) / den;

        DoubleByReference ssreans = new DoubleByReference();
        DoubleByReference ssimans = new DoubleByReference();
        assertEquals(0, core.cholmod_divcomplex(re, im, divre, divim, ssreans, ssimans));
        assertEquals(reans, ssreans.doubleValue());
        assertEquals(imans, ssimans.doubleValue());
    }

    @Test
    public void cholmod_l_divcomplex()
    {
        double re = 10.0;
        double im = 38.0;
        double divre = 2.0;
        double divim = 16;

        double den = divre * divre + divim * divim;
        double reans = (re * divre + im * divim) / den;
        double imans = (im * divre - re * divim) / den;

        DoubleByReference ssreans = new DoubleByReference();
        DoubleByReference ssimans = new DoubleByReference();
        assertEquals(0, core.cholmod_l_divcomplex(re, im, divre, divim, ssreans, ssimans));
        assertEquals(reans, ssreans.doubleValue());
        assertEquals(imans, ssimans.doubleValue());
    }

    private void testSparse(cholmod_sparse A, IType iType, int nzmax)
    {
        assertNotEquals(null, Struct.getMemory(A));
        assertEquals(25, A.nrow.intValue());
        assertEquals(25, A.ncol.intValue());
        assertEquals(nzmax, A.nzmax.intValue());
        assertEquals(DType.CHOLMOD_DOUBLE, A.dtype.get()); // is only ever double
        assertEquals(cholmod_sparse.SType.Unsymmetric,
                A.stype.get());
        assertEquals(XType.CHOLMOD_REAL, A.xtype.get());
        assertEquals(iType, A.itype.get());
        assertEquals(1, A.sorted.intValue());
        assertEquals(1, A.packed.intValue());
        assertEquals(null, A.nz.get());
        assertEquals(null, A.z.get());
        assertNotEquals(null, A.p.get());
        assertNotEquals(null, A.i.get());
        assertNotEquals(null, A.x.get());
    }

    @Test
    public void cholmod_allocate_sparse()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_start(cc));
        cholmod_sparse A = core.cholmod_allocate_sparse(25, 25, 100, 1,
                1, cholmod_sparse.SType.Unsymmetric, XType.CHOLMOD_REAL, cc);
        testSparse(A, IType.CHOLMOD_INT, 100);
        A.x.get().putDouble(99, 123.456);
        assertEquals(1, core.cholmod_reallocate_sparse(200, A, cc));
        testSparse(A, IType.CHOLMOD_INT, 200);
        assertEquals(123.456,A.x.get().getDouble(99));
        assertEquals(1, Core.Cholmod_Free_Sparse(core, A, cc));
        assertEquals(null, A.p.get());
        assertEquals(null, A.i.get());
        assertEquals(null, A.x.get());
        assertEquals(1, core.cholmod_finish(cc));
    }

    @Test
    public void cholmod_l_allocate_sparse()
    {
        cholmod_common cc = new cholmod_common(Runtime.getSystemRuntime());
        assertEquals(1, core.cholmod_l_start(cc));
        cholmod_sparse A = core.cholmod_l_allocate_sparse(25, 25, 100, 1,
                1, cholmod_sparse.SType.Unsymmetric, XType.CHOLMOD_REAL, cc);
        testSparse(A, IType.CHOLMOD_LONG, 100);
        A.x.get().putDouble(99, 123.456);
        assertEquals(1, core.cholmod_l_reallocate_sparse(200, A, cc));
        testSparse(A, IType.CHOLMOD_LONG, 200);
        assertEquals(123.456,A.x.get().getDouble(99));
        assertEquals(1, Core.Cholmod_l_Free_Sparse(core, A, cc));
        assertEquals(null, A.p.get());
        assertEquals(null, A.i.get());
        assertEquals(null, A.x.get());
        assertEquals(1, core.cholmod_l_finish(cc));
    }

}