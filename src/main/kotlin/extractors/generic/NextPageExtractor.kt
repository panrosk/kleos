package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor

private val DEFAULT_NEXT_SELECTORS = listOf(
    "a[rel=next]",
    "a[aria-label*=next i]",
    "a[aria-label*=siguiente i]",
    "a[class*=next]",
    "a[id*=next]",
    "a[href*='page/2']",
    "a[href*='?page=2']",
    "a:containsOwn(Next)",
    "a:containsOwn(Siguiente)",
)

class NextPageExtractor(
    private val selectors: List<String> = DEFAULT_NEXT_SELECTORS
) : Extractor {

    override fun extract(doc: Document): String? {
        for (selector in selectors) {
            val element = doc.selectFirst(selector) ?: continue
            val href = element.attr("href").trim()
            if (href.isNotEmpty()) {
                return href
            }
        }
        return null
    }
}