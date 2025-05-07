package notiq.kleos.cleaners

class CleanAuthor:Cleaner {
    override fun clean(str: String): String {
        return  str.removePrefix("By ").removePrefix("by ").trim()
    }
}