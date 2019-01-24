package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_common extends Struct
{
    public  static final int CHOLMOD_MAXMETHODS = 9;
    public final Double dbound = new Double();
    public final Double grow0 = new Double();
    public final Double grow1 = new Double();
    public final size_t grow2 = new size_t();
    public final size_t maxrank = new size_t();
    public final Double supernodal_switch = new Double();
    public final int32_t supernodal = new int32_t();
    public final int32_t final_asis = new int32_t();
    public final int32_t final_super = new int32_t();
    public final int32_t final_ll = new int32_t();
    public final int32_t final_pack = new int32_t();
    public final int32_t final_monotonic = new int32_t();
    public final int32_t final_resymbol = new int32_t();
    public final Double[] zrelax = array(new Double[3]);
    public final size_t[] nrelax = array(new size_t[3]);
    public final int32_t prefer_zomplex = new int32_t();
    public final int32_t prefer_upper = new int32_t();
    public final int32_t quick_return_if_not_posdef = new int32_t();
    public final int32_t prefer_binary = new int32_t();
    public final int32_t print = new int32_t();
    public final int32_t precise = new int32_t();
    public final int32_t try_catch = new int32_t();
    public final Function<ErrorHandler> error_handler = new Function<>(ErrorHandler.class);
    public final int32_t nmethods = new int32_t();
    public final int32_t current = new int32_t();
    public final int32_t selected = new int32_t();
    public final CholmodMethod method[] = array(new CholmodMethod[CHOLMOD_MAXMETHODS+1]);
    public final int32_t postorder = new int32_t();
    public final int32_t default_nesdis = new int32_t();
    public final Double metis_memory = new Double();
    public final Double metis_dswitch = new Double();
    public final size_t metis_nswitch = new size_t();
    public final size_t nrow = new size_t();
    /** TODO:  handle SuiteSparse_long more flexibly if possible */
    public final int64_t mark = new int64_t();
    public final size_t iworksize = new size_t();
    public final size_t xworksize = new size_t();
    public final Pointer Flag = new Pointer();
    public final Pointer Head = new Pointer();
    public final Pointer Xwork = new Pointer();
    public final Pointer Iwork = new Pointer();
    public final Enum<ssjava.cholmod.IType> itype = new Enum(ssjava.cholmod.IType.class);
    public final Enum<ssjava.cholmod.DType> dtype = new Enum(ssjava.cholmod.DType.class);
    public final int32_t no_workspace_reallocate = new int32_t();
    public final Enum<CholmodStatus> status = new Enum<>(CholmodStatus.class);
    public final Double fl = new Double();
    public final Double lnz = new Double();
    public final Double anz = new Double();
    public final Double modfl = new Double();
    public final size_t malloc_count = new size_t();
    public final size_t memory_usage = new size_t();
    public final size_t memory_inuse = new size_t();
    public final Double nrealloc_col = new Double();
    public final Double nrealloc_factor = new Double();
    public final Double ndbounds_hit = new Double();
    public final Double rowfacfl = new Double();
    public final Double aatfl = new Double();
    public final int32_t called_nd = new int32_t();
    public final int32_t blas_ok = new int32_t();
    public final Double SPQR_grain = new Double();
    public final Double SPQR_small = new Double();
    public final int32_t SPQR_shrink = new int32_t();
    public final int32_t SPQR_nthreads = new int32_t();
    public final Double SPQR_flopcount = new Double();
    public final Double SPQR_analyze_time = new Double();
    public final Double SPQR_factorize_time = new Double();
    public final Double SPQR_solve_time = new Double();
    public final Double SPQR_flopcount_bound = new Double();
    public final Double SPQR_tol_used = new Double();
    public final Double SPQR_norm_E_fro = new Double();
    /** TODO:  handle SuiteSparse_long more flexibly if possible */
    public final int64_t[] SPQR_istat = array(new int64_t[10]);
    public final Enum<UseGPU> useGPU = new Enum<>(UseGPU.class);
    public final size_t maxGpuMemBytes = new size_t();
    public final Double maxGpuMemFraction = new Double();
    public final size_t gpuMemorySize = new size_t();
    public final Double gpuKernelTime = new Double();
    /** TODO:  handle SuiteSparse_long more flexibly if possible */
    public final int64_t gpuFlops = new int64_t();
    public final int32_t gpuNumKernelLaunches = new int32_t();
    public final Pointer cublasHandle = new Pointer();
    public final Pointer[] gpuStream = array(new Pointer[Core.CHOLMOD_HOST_SUPERNODE_BUFFERS]);
    public final Pointer[] cublasEventPotrf = array(new Pointer[3]);
    public final Pointer updateCKernelsComplete = new Pointer();
    public final Pointer[] updateCBuffersFree = array(new Pointer[Core.CHOLMOD_HOST_SUPERNODE_BUFFERS]);
    public final Pointer dev_mempool = new Pointer();
    public final size_t dev_mempool_size = new size_t();
    public final Pointer host_pinned_mempool = new Pointer();
    public final size_t host_pinned_mempool_size = new size_t();
    public final size_t devBuffSize = new size_t();
    public final int32_t ibuffer = new int32_t();
    public final Double syrkStart = new Double();
    public final Double cholmod_cpu_gemm_time = new Double();
    public final Double cholmod_cpu_syrk_time = new Double();
    public final Double cholmod_cpu_trsm_time = new Double();
    public final Double cholmod_cpu_potrf_time = new Double();
    public final Double cholmod_gpu_gemm_time = new Double();
    public final Double cholmod_gpu_syrk_time = new Double();
    public final Double cholmod_gpu_trsm_time = new Double();
    public final Double cholmod_gpu_potrf_time = new Double();
    public final Double cholmod_assemble_time = new Double();
    public final Double cholmod_assemble_time2 = new Double();
    public final size_t cholmod_cpu_gemm_calls = new size_t();
    public final size_t cholmod_cpu_syrk_calls = new size_t();
    public final size_t cholmod_cpu_trsm_calls = new size_t();
    public final size_t cholmod_cpu_potrf_calls = new size_t();
    public final size_t cholmod_gpu_gemm_calls = new size_t();
    public final size_t cholmod_gpu_syrk_calls = new size_t();
    public final size_t cholmod_gpu_trsm_calls = new size_t();
    public final size_t cholmod_gpu_potrf_calls = new size_t();

    public cholmod_common()
    {
        this(Runtime.getSystemRuntime());
    }
    public cholmod_common(Runtime runtime)
    {
        super(runtime);
    }
}
