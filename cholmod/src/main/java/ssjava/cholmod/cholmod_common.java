package ssjava.cholmod;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class cholmod_common extends Struct
{
    public static final int CHOLMOD_MAXMETHODS = 9;
    private final Double dbound = new Double();
    private final Double grow0 = new Double();
    private final Double grow1 = new Double();
    private final size_t grow2 = new size_t();
    private final size_t maxrank = new size_t();
    private final Double supernodal_switch = new Double();
    private final Enum<SupernodalStrategy> supernodal = new Enum(SupernodalStrategy.class);
    private final int32_t final_asis = new int32_t();
    private final int32_t final_super = new int32_t();
    private final int32_t final_ll = new int32_t();
    private final int32_t final_pack = new int32_t();
    private final int32_t final_monotonic = new int32_t();
    private final int32_t final_resymbol = new int32_t();
    private final Double[] zrelax = array(new Double[3]);
    private final size_t[] nrelax = array(new size_t[3]);
    private final int32_t prefer_zomplex = new int32_t();
    private final int32_t prefer_upper = new int32_t();
    private final int32_t quick_return_if_not_posdef = new int32_t();
    private final int32_t prefer_binary = new int32_t();
    private final int32_t print = new int32_t();
    private final int32_t precise = new int32_t();
    private final int32_t try_catch = new int32_t();
    private final Function<ErrorHandler> error_handler = new Function<>(ErrorHandler.class);
    private final int32_t nmethods = new int32_t();
    private final int32_t current = new int32_t();
    private final int32_t selected = new int32_t();
    private final CholmodMethod method[] = array(new CholmodMethod[CHOLMOD_MAXMETHODS + 1]);
    private final int32_t postorder = new int32_t();
    private final int32_t default_nesdis = new int32_t();
    private final Double metis_memory = new Double();
    private final Double metis_dswitch = new Double();
    private final size_t metis_nswitch = new size_t();
    private final size_t nrow = new size_t();
    /**
     * TODO:  handle SuiteSparse_long more flexibly if possible
     */
    private final int64_t mark = new int64_t();
    private final size_t iworksize = new size_t();
    private final size_t xworksize = new size_t();
    private final Pointer Flag = new Pointer();
    private final Pointer Head = new Pointer();
    private final Pointer Xwork = new Pointer();
    private final Pointer Iwork = new Pointer();
    private final Enum<ssjava.cholmod.IType> itype = new Enum(ssjava.cholmod.IType.class);
    private final Enum<ssjava.cholmod.DType> dtype = new Enum(ssjava.cholmod.DType.class);
    private final int32_t no_workspace_reallocate = new int32_t();
    private final Enum<CholmodStatus> status = new Enum<>(CholmodStatus.class);
    private final Double fl = new Double();
    private final Double lnz = new Double();
    private final Double anz = new Double();
    private final Double modfl = new Double();
    private final size_t malloc_count = new size_t();
    private final size_t memory_usage = new size_t();
    private final size_t memory_inuse = new size_t();
    private final Double nrealloc_col = new Double();
    private final Double nrealloc_factor = new Double();
    private final Double ndbounds_hit = new Double();
    private final Double rowfacfl = new Double();
    private final Double aatfl = new Double();
    private final int32_t called_nd = new int32_t();
    private final int32_t blas_ok = new int32_t();
    private final Double SPQR_grain = new Double();
    private final Double SPQR_small = new Double();
    private final int32_t SPQR_shrink = new int32_t();
    private final int32_t SPQR_nthreads = new int32_t();
    private final Double SPQR_flopcount = new Double();
    private final Double SPQR_analyze_time = new Double();
    private final Double SPQR_factorize_time = new Double();
    private final Double SPQR_solve_time = new Double();
    private final Double SPQR_flopcount_bound = new Double();
    private final Double SPQR_tol_used = new Double();
    private final Double SPQR_norm_E_fro = new Double();
    /**
     * TODO:  handle SuiteSparse_long more flexibly if possible
     */
    private final int64_t[] SPQR_istat = array(new int64_t[10]);
    private final Enum<UseGPU> useGPU = new Enum<>(UseGPU.class);
    private final size_t maxGpuMemBytes = new size_t();
    private final Double maxGpuMemFraction = new Double();
    private final size_t gpuMemorySize = new size_t();
    private final Double gpuKernelTime = new Double();
    /**
     * TODO:  handle SuiteSparse_long more flexibly if possible
     */
    private final int64_t gpuFlops = new int64_t();
    private final int32_t gpuNumKernelLaunches = new int32_t();
    private final Pointer cublasHandle = new Pointer();
    private final Pointer[] gpuStream = array(new Pointer[Core.CHOLMOD_HOST_SUPERNODE_BUFFERS]);
    private final Pointer[] cublasEventPotrf = array(new Pointer[3]);
    private final Pointer updateCKernelsComplete = new Pointer();
    private final Pointer[] updateCBuffersFree = array(new Pointer[Core.CHOLMOD_HOST_SUPERNODE_BUFFERS]);
    private final Pointer dev_mempool = new Pointer();
    private final size_t dev_mempool_size = new size_t();
    private final Pointer host_pinned_mempool = new Pointer();
    private final size_t host_pinned_mempool_size = new size_t();
    private final size_t devBuffSize = new size_t();
    private final int32_t ibuffer = new int32_t();
    private final Double syrkStart = new Double();
    private final Double cholmod_cpu_gemm_time = new Double();
    private final Double cholmod_cpu_syrk_time = new Double();
    private final Double cholmod_cpu_trsm_time = new Double();
    private final Double cholmod_cpu_potrf_time = new Double();
    private final Double cholmod_gpu_gemm_time = new Double();
    private final Double cholmod_gpu_syrk_time = new Double();
    private final Double cholmod_gpu_trsm_time = new Double();
    private final Double cholmod_gpu_potrf_time = new Double();
    private final Double cholmod_assemble_time = new Double();
    private final Double cholmod_assemble_time2 = new Double();
    private final size_t cholmod_cpu_gemm_calls = new size_t();
    private final size_t cholmod_cpu_syrk_calls = new size_t();
    private final size_t cholmod_cpu_trsm_calls = new size_t();
    private final size_t cholmod_cpu_potrf_calls = new size_t();
    private final size_t cholmod_gpu_gemm_calls = new size_t();
    private final size_t cholmod_gpu_syrk_calls = new size_t();
    private final size_t cholmod_gpu_trsm_calls = new size_t();
    private final size_t cholmod_gpu_potrf_calls = new size_t();

    public cholmod_common()
    {
        this(Runtime.getSystemRuntime());
    }

    public cholmod_common(Runtime runtime)
    {
        super(runtime);
    }

    public double getDbound()
    {
        return dbound.doubleValue();
    }

    public void setDbound(double dbound)
    {
        this.dbound.set(dbound);
    }

    public double getGrow0()
    {
        return grow0.doubleValue();
    }

    public void setGrow0(double grow0)
    {
        this.grow0.set(grow0);
    }

    public double getGrow1()
    {
        return grow1.doubleValue();
    }

    public void setGrow1(double grow1)
    {
        this.grow1.set(grow1);
    }

    public long getGrow2()
    {
        return grow2.longValue();
    }

    public void setGrow2(long grow2)
    {
        this.grow2.set(grow2);
    }

    public long getMaxrank()
    {
        return maxrank.longValue();
    }

    public void setMaxrank(long maxrank)
    {
        this.maxrank.set(maxrank);
    }

    public double getSupernodal_switch()
    {
        return supernodal_switch.doubleValue();
    }

    public void setSupernodal_switch(double supernodal_switch)
    {
        this.supernodal_switch.set(supernodal_switch);
    }

    public SupernodalStrategy getSupernodal()
    {
        return supernodal.get();
    }

    public void setSupernodal(SupernodalStrategy supernodal)
    {
        this.supernodal.set(supernodal);
    }

    public boolean getFinal_asis()
    {
        return final_asis.intValue() != 0;
    }

    public void setFinal_asis(boolean final_asis)
    {
        this.final_asis.set(final_asis ? 1 : 0);
    }

    public boolean getFinal_super()
    {
        return final_super.intValue() != 0;
    }

    public void setFinal_super(boolean final_super)
    {
        this.final_super.set(final_super ? 1 : 0);
    }

    public boolean getFinal_ll()
    {
        return final_ll.intValue() != 0;
    }

    public void setFinal_ll(boolean final_ll)
    {
        this.final_ll.set(final_ll ? 1 : 0);
    }

    public boolean getFinal_pack()
    {
        return final_pack.intValue() != 0;
    }

    public void setFinal_pack(boolean final_pack)
    {
        this.final_pack.set(final_pack ? 1 : 0);
    }

    public boolean getFinal_monotonic()
    {
        return final_monotonic.intValue() != 0;
    }

    public void setFinal_monotonic(boolean final_monotonic)
    {
        this.final_monotonic.set(final_monotonic ? 1 : 0);
    }

    public boolean getFinal_resymbol()
    {
        return final_resymbol.intValue() != 0;
    }

    public void setFinal_resymbol(boolean final_resymbol)
    {
        this.final_resymbol.set(final_resymbol ? 1 : 0);
    }

    public Double[] getZrelax()
    {
        return zrelax;
    }

    public double getZrelax(int index)
    {
        return zrelax[index].doubleValue();
    }

    public void setZrelax(int index, double zrelax)
    {
        this.zrelax[index].set(zrelax);
    }

    public size_t[] getNrelax()
    {
        return nrelax;
    }

    public long getNrelax(int index)
    {
        return nrelax[index].longValue();
    }

    public void setNrelax(int index, long nrelax)
    {
        this.nrelax[index].set(nrelax);
    }

    public boolean getPrefer_zomplex()
    {
        return prefer_zomplex.intValue() != 0;
    }

    public void setPrefer_zomplex(boolean prefer_zomplex)
    {
        this.prefer_zomplex.set(prefer_zomplex ? 1 : 0);
    }

    public boolean getPrefer_upper()
    {
        return prefer_upper.intValue() != 0;
    }

    public void setPrefer_upper(boolean prefer_upper)
    {
        this.prefer_upper.set(prefer_upper ? 1 : 0);
    }

    public boolean getQuick_return_if_not_posdef()
    {
        return quick_return_if_not_posdef.intValue() != 0;
    }

    public void setQuick_return_if_not_posdef(boolean quick_return_if_not_posdef)
    {
        this.quick_return_if_not_posdef.set(quick_return_if_not_posdef ? 1 : 0);
    }

    public boolean getPrefer_binary()
    {
        return prefer_binary.intValue() != 0;
    }

    public void setPrefer_binary(boolean prefer_binary)
    {
        this.prefer_binary.set(prefer_binary ? 1 : 0);
    }

    public int getPrint()
    {
        return print.intValue();
    }

    public void setPrint(int printLevel)
    {
        this.print.set(printLevel);
    }

    public boolean getPrecise()
    {
        return precise.intValue() != 0;
    }

    public void setPrecise(boolean precise)
    {
        this.precise.set(precise ? 1 : 0);
    }

    public boolean getTry_catch()
    {
        return try_catch.intValue() != 0;
    }

    public void setTry_catch(boolean try_catch)
    {
        this.try_catch.set(try_catch ? 1 : 0);
    }

    public void setError_handler(ErrorHandler eh)
    {
        error_handler.set(eh);
    }

    public int getNmethods()
    {
        return nmethods.intValue();
    }

    public void setNmethods(int nmethods)
    {
        this.nmethods.set(nmethods);
    }

    public int getCurrent()
    {
        return current.intValue();
    }

    public int getSelected()
    {
        return selected.intValue();
    }

    public CholmodMethod getMethod(int imeth)
    {
        return method[imeth];
    }

    public boolean getPostorder()
    {
        return postorder.intValue() != 0;
    }

    public void setPostorder(boolean postorder)
    {
        this.postorder.set(postorder ? 1 : 0);
    }

    public boolean getDefault_nesdis()
    {
        return default_nesdis.intValue() != 0;
    }

    public void setDefault_nesdis(boolean default_nesdis)
    {
        this.default_nesdis.set(default_nesdis ? 1 : 0);
    }

    public double getMetis_memory()
    {
        return metis_memory.doubleValue();
    }

    public void setMetis_memory(double metis_memory)
    {
        this.metis_memory.set(metis_memory);
    }

    public double getMetis_dswitch()
    {
        return metis_dswitch.doubleValue();
    }

    public void setMetis_dswitch(double metis_dswitch)
    {
        this.metis_dswitch.set(metis_dswitch);
    }

    public long getMetis_nswitch()
    {
        return metis_nswitch.longValue();
    }

    public void setMetis_nswitch(long metis_nswitch)
    {
        this.metis_nswitch.set(metis_nswitch);
    }

    public long getNrow()
    {
        return nrow.longValue();
    }

    public long getMark()
    {
        return mark.longValue();
    }

    public long getIworksize()
    {
        return iworksize.longValue();
    }

    public long getXworksize()
    {
        return xworksize.longValue();
    }

    public jnr.ffi.Pointer getFlag()
    {
        return Flag.get();
    }

    public jnr.ffi.Pointer getHead()
    {
        return Head.get();
    }

    public jnr.ffi.Pointer getXwork()
    {
        return Xwork.get();
    }

    public jnr.ffi.Pointer getIwork()
    {
        return Iwork.get();
    }

    public IType getItype()
    {
        return itype.get();
    }

    public DType getDtype()
    {
        return dtype.get();
    }

    public boolean getNo_workspace_reallocate()
    {
        return no_workspace_reallocate.intValue() != 0;
    }

    public void setNo_workspace_reallocate(boolean no_workspace_reallocate)
    {
        this.no_workspace_reallocate.set(no_workspace_reallocate ? 1 : 0);
    }

    public CholmodStatus getStatus()
    {
        return status.get();
    }

    public double getFl()
    {
        return fl.doubleValue();
    }

    public double getLnz()
    {
        return lnz.doubleValue();
    }

    public double getAnz()
    {
        return anz.doubleValue();
    }

    public double getModfl()
    {
        return modfl.doubleValue();
    }

    public long getMalloc_count()
    {
        return malloc_count.longValue();
    }

    public long getMemory_usage()
    {
        return memory_usage.longValue();
    }

    public long getMemory_inuse()
    {
        return memory_inuse.longValue();
    }

    public double getNrealloc_col()
    {
        return nrealloc_col.doubleValue();
    }

    public double getNrealloc_factor()
    {
        return nrealloc_factor.doubleValue();
    }

    public double getNdbounds_hit()
    {
        return ndbounds_hit.doubleValue();
    }

    public double getRowfacfl()
    {
        return rowfacfl.doubleValue();
    }

    public double getAatfl()
    {
        return aatfl.doubleValue();
    }

    public int getCalled_nd()
    {
        return called_nd.intValue();
    }

    public int getBlas_ok()
    {
        return blas_ok.intValue();
    }

    public double getSPQR_grain()
    {
        return SPQR_grain.intValue();
    }

    public void setSPQR_grain(double spqr_grain)
    {
        this.SPQR_grain.set(spqr_grain);
    }

    public double getSPQR_small()
    {
        return SPQR_small.doubleValue();
    }

    public void setSPQR_small(double spqr_small)
    {
        SPQR_small.set(spqr_small);
    }

    public int getSPQR_shrink()
    {
        return SPQR_shrink.intValue();
    }

    public void setSPQR_shrink(int spqr_shrink)
    {
        SPQR_shrink.set(spqr_shrink);
    }

    public int getSPQR_nthreads()
    {
        return SPQR_nthreads.intValue();
    }

    public void setSPQR_nthreads(int spqr_nthreads)
    {
        SPQR_nthreads.set(spqr_nthreads);
    }

    public double getSPQR_flopcount()
    {
        return SPQR_flopcount.doubleValue();
    }

    public double getSPQR_analyze_time()
    {
        return SPQR_analyze_time.doubleValue();
    }

    public double getSPQR_factorize_time()
    {
        return SPQR_factorize_time.doubleValue();
    }

    public double getSPQR_solve_time()
    {
        return SPQR_solve_time.doubleValue();
    }

    public double getSPQR_flopcount_bound()
    {
        return SPQR_flopcount_bound.doubleValue();
    }

    public double getSPQR_tol_used()
    {
        return SPQR_tol_used.doubleValue();
    }

    public double getSPQR_norm_E_fro()
    {
        return SPQR_norm_E_fro.doubleValue();
    }

    public UseGPU getUseGPU()
    {
        return useGPU.get();
    }

    public void setUseGPU(UseGPU useGPU)
    {
        this.useGPU.set(useGPU);
    }

    public long getMaxGpuMemBytes()
    {
        return maxGpuMemBytes.longValue();
    }

    public double getMaxGpuMemFraction()
    {
        return maxGpuMemFraction.doubleValue();
    }

    public long getGpuMemorySize()
    {
        return gpuMemorySize.longValue();
    }

    public double getGpuKernelTime()
    {
        return gpuKernelTime.doubleValue();
    }

    public long getGpuFlops()
    {
        return gpuFlops.longValue();
    }

    public int getGpuNumKernelLaunches()
    {
        return gpuNumKernelLaunches.intValue();
    }

    public jnr.ffi.Pointer getCublasHandle()
    {
        return cublasHandle.get();
    }

    public jnr.ffi.Pointer getUpdateCKernelsComplete()
    {
        return updateCKernelsComplete.get();
    }

    public jnr.ffi.Pointer getDev_mempool()
    {
        return dev_mempool.get();
    }

    public long getDev_mempool_size()
    {
        return dev_mempool_size.longValue();
    }

    public jnr.ffi.Pointer getHost_pinned_mempool()
    {
        return host_pinned_mempool.get();
    }

    public long getHost_pinned_mempool_size()
    {
        return host_pinned_mempool_size.longValue();
    }

    public int getIbuffer()
    {
        return ibuffer.intValue();
    }

    public double getSyrkStart()
    {
        return syrkStart.doubleValue();
    }

    public double getCholmod_cpu_gemm_time()
    {
        return cholmod_cpu_gemm_time.doubleValue();
    }

    public double getCholmod_cpu_syrk_time()
    {
        return cholmod_cpu_syrk_time.doubleValue();
    }

    public double getCholmod_cpu_trsm_time()
    {
        return cholmod_cpu_trsm_time.doubleValue();
    }

    public double getCholmod_cpu_potrf_time()
    {
        return cholmod_cpu_potrf_time.doubleValue();
    }

    public double getCholmod_gpu_gemm_time()
    {
        return cholmod_gpu_gemm_time.doubleValue();
    }

    public double getCholmod_gpu_syrk_time()
    {
        return cholmod_gpu_syrk_time.doubleValue();
    }

    public double getCholmod_gpu_trsm_time()
    {
        return cholmod_gpu_trsm_time.doubleValue();
    }

    public double getCholmod_gpu_potrf_time()
    {
        return cholmod_gpu_potrf_time.doubleValue();
    }

    public double getCholmod_assemble_time()
    {
        return cholmod_assemble_time.doubleValue();
    }

    public double getCholmod_assemble_time2()
    {
        return cholmod_assemble_time2.doubleValue();
    }

    public long getCholmod_cpu_gemm_calls()
    {
        return cholmod_cpu_gemm_calls.longValue();
    }

    public long getCholmod_cpu_syrk_calls()
    {
        return cholmod_cpu_syrk_calls.longValue();
    }

    public long getCholmod_cpu_trsm_calls()
    {
        return cholmod_cpu_trsm_calls.longValue();
    }

    public long getCholmod_cpu_potrf_calls()
    {
        return cholmod_cpu_potrf_calls.longValue();
    }

    public long getCholmod_gpu_gemm_calls()
    {
        return cholmod_gpu_gemm_calls.longValue();
    }

    public long getCholmod_gpu_syrk_calls()
    {
        return cholmod_gpu_syrk_calls.longValue();
    }

    public long getCholmod_gpu_trsm_calls()
    {
        return cholmod_gpu_trsm_calls.longValue();
    }

    public long getCholmod_gpu_potrf_calls()
    {
        return cholmod_gpu_potrf_calls.longValue();
    }

    public int64_t[] getSPQR_istat()
    {
        return SPQR_istat;
    }
}



