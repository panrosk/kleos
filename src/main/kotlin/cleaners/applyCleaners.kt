package notiq.kleos.cleaners

fun applyCleaners(input: String, cleaners: List<Cleaner>): String {
    return cleaners.fold(input) { acc, cleaner -> cleaner.clean(acc) }
}