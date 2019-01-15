package ssjava.cholmod;

/**
 * Ordering for sparsity preservation
 */

public enum Order
{
    CHOLMOD_NATURAL,
    CHOLMOD_GIVEN,
    CHOLMOD_AMD,
    CHOLMOD_METIS,
    CHOLMOD_NESDIS,
    CHOLMOD_COLAMD,
    CHOLMOD_POSTORDERED

}
