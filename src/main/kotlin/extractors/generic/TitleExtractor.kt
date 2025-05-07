package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor

private val DEFAULT_TITLE_META_TAGS = listOf(
    "og:title", "title", "twitter:title"
)

private val DEFAULT_TITLE_SELECTORS = listOf(
    "h1.title", ".post-title", "h1.entry-title", "header h1", "h1"
)

private const val DEFAULT_TITLE_MAX_LENGTH = 300

class TitleExtractor(
    private val metaTags: List<String> = DEFAULT_TITLE_META_TAGS,
    private val cssSelectors: List<String> = DEFAULT_TITLE_SELECTORS,
    private val maxLength: Int = DEFAULT_TITLE_MAX_LENGTH
) : Extractor {

    override fun extract(doc: Document): String? {
        // 1. Buscar en meta tags
        for (metaName in metaTags) {
            val meta = doc.selectFirst("""meta[property="$metaName"], meta[name="$metaName"]""") ?: continue
            val content = meta.attr("content")?.trim()
            if (!content.isNullOrEmpty() && content.length < maxLength) {
                return content
            }
        }

        // 2. Buscar en selectores comunes
        for (selector in cssSelectors) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (text.isNotEmpty() && text.length < maxLength) {
                return text
            }
        }

        // 3. Usar <title> como fallback
        val titleTag = doc.selectFirst("title")
        val titleText = titleTag?.text()?.trim()
        if (!titleText.isNullOrEmpty() && titleText.length < maxLength) {
            return titleText
        }

        return null
    }
}