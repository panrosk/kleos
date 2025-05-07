
/**
 * A Cleaner transforms a string into a normalized version.
 *
 * Implementations MUST be idempotent and pure.
 * That means:
 * - `clean(clean(x)) == clean(x)`
 * - `clean(x)` always returns the same result for the same input `x`
 */


package notiq.kleos.cleaners
fun interface Cleaner {
    fun clean(input: String): String
}// 1. Canonical <link>
