package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor

private val DEFAULT_META_TAGS = listOf(
    "og:url", "canonical", "twitter:url"
)

class UrlExtractor(
    private val metaTags: List<String> = DEFAULT_META_TAGS
) : Extractor {

    override fun extract(doc: Document): String? {
        val canonicalLink = doc.selectFirst("link[rel=canonical]")
        val canonicalHref = canonicalLink?.attr("href")?.trim()
        if (!canonicalHref.isNullOrEmpty()) {
            return canonicalHref
        }

        for (metaName in metaTags) {
            val meta = doc.selectFirst("""meta[property="$metaName"], meta[name="$metaName"]""")
            val content = meta?.attr("content")?.trim()
            if (!content.isNullOrEmpty()) {
                return content
            }
        }

        return doc.baseUri().takeIf { it.isNotEmpty() }
    }
}