package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor

private val TITLE_META_TAGS = listOf(
    "og:title", "title", "twitter:title"
)

private const val TITLE_MAX_LENGTH = 300

private val TITLE_SELECTORS = listOf(
    "h1.title", ".post-title", "h1.entry-title", "header h1", "h1"
)

class TitleExtractor : Extractor {

    override fun extract(doc: Document): String? {
        // 1. Buscar en meta tags
        for (metaName in TITLE_META_TAGS) {
            val meta = doc.selectFirst("""meta[property="$metaName"], meta[name="$metaName"]""") ?: continue
            val content = meta.attr("content")?.trim()
            if (!content.isNullOrEmpty() && content.length < TITLE_MAX_LENGTH) {
                return content
            }
        }

        // 2. Buscar en selectores comunes
        for (selector in TITLE_SELECTORS) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (text.isNotEmpty() && text.length < TITLE_MAX_LENGTH) {
                return text
            }
        }

        // 3. Usar <title> como fallback
        val titleTag = doc.selectFirst("title")
        val titleText = titleTag?.text()?.trim()
        if (!titleText.isNullOrEmpty() && titleText.length < TITLE_MAX_LENGTH) {
            return titleText
        }

        return null
    }
}