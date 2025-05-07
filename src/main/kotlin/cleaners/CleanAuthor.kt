package notiq.kleos.cleaners

class CleanAuthor:Cleaners {
    override fun clean(str: String): String? {
        return  str.removePrefix("By ").removePrefix("by ").trim()
    }
}