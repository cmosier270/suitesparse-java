package ssjava.cholmod;

import jnr.ffi.annotations.Out;

@FunctionalInterface
public interface ErrorHandler
{
    void handle(@Out int status, @Out String file, @Out int line, @Out String message);
}
