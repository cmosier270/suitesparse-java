package ssjava.cholmod;

import jnr.ffi.annotations.Delegate;
import jnr.ffi.annotations.Out;

@FunctionalInterface
public interface ErrorHandler
{
    @Delegate
    void report(@Out CholmodStatus status, @Out String file, @Out int line, @Out String message);
}
