package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor
import notiq.kleos.cleaners.CleanAuthor

private val AUTHOR_META_TAGS = listOf(
    "byl", "clmst", "dc.author", "dcsext.author",
    "dc.creator", "rbauthors", "authors"
)

private const val AUTHOR_MAX_LENGTH = 300

private val AUTHOR_SELECTORS = listOf(
    ".entry .entry-author", ".author.vcard .fn", ".author .vcard .fn",
    ".byline.vcard .fn", ".byline .vcard .fn", ".byline .by .author",
    ".byline .by", ".byline .author", ".post-author.vcard",
    ".post-author .vcard", "a[rel=author]", "#by_author", ".by_author",
    "#entryAuthor", ".entryAuthor", ".byline a[href*=author]",
    "#author .authorname", ".author .authorname", "#author",
    ".author", ".articleauthor", ".ArticleAuthor", ".byline"
)

private val BYLINE_SELECTORS_RE = listOf(
    "#byline" to Regex("""^\s*By""", RegexOption.IGNORE_CASE),
    ".byline" to Regex("""^\s*By""", RegexOption.IGNORE_CASE)
)

class AuthorExtractor : Extractor {

    private val cleaner = CleanAuthor()

    override fun extract(doc: Document): String? {
        for (metaName in AUTHOR_META_TAGS) {
            val meta = doc.selectFirst("meta[name=$metaName]") ?: continue
            val content = meta.attr("content")?.trim()
            if (!content.isNullOrEmpty() && content.length < AUTHOR_MAX_LENGTH) {
                return cleaner.clean(content)
            }
        }

        for (selector in AUTHOR_SELECTORS) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (text.isNotEmpty() && text.length < AUTHOR_MAX_LENGTH) {
                return cleaner.clean(text)
            }
        }

        for ((selector, regex) in BYLINE_SELECTORS_RE) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (regex.containsMatchIn(text)) {
                return cleaner.clean(text)
            }
        }

        return null
    }
}