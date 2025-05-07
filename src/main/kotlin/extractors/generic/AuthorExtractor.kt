package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor
import notiq.kleos.cleaners.Cleaner
import notiq.kleos.cleaners.applyCleaners

private const val AUTHOR_MAX_LENGTH = 300

private val DEFAULT_META_TAGS = listOf(
    "byl", "clmst", "dc.author", "dcsext.author",
    "dc.creator", "rbauthors", "authors"
)

private val DEFAULT_SELECTORS = listOf(
    ".entry .entry-author", ".author.vcard .fn", ".author .vcard .fn",
    ".byline.vcard .fn", ".byline .vcard .fn", ".byline .by .author",
    ".byline .by", ".byline .author", ".post-author.vcard",
    ".post-author .vcard", "a[rel=author]", "#by_author", ".by_author",
    "#entryAuthor", ".entryAuthor", ".byline a[href*=author]",
    "#author .authorname", ".author .authorname", "#author",
    ".author", ".articleauthor", ".ArticleAuthor", ".byline"
)

private val DEFAULT_BYLINE_REGEX_SELECTORS = listOf(
    "#byline" to Regex("""^\s*By""", RegexOption.IGNORE_CASE),
    ".byline" to Regex("""^\s*By""", RegexOption.IGNORE_CASE)
)

class AuthorExtractor(
    private val metaTags: List<String> = DEFAULT_META_TAGS,
    private val cssSelectors: List<String> = DEFAULT_SELECTORS,
    private val regexSelectors: List<Pair<String, Regex>> = DEFAULT_BYLINE_REGEX_SELECTORS,
    private val cleaners: List<Cleaner> = emptyList()
) : Extractor {

    override fun extract(doc: Document): String? {
        for (metaName in metaTags) {
            val meta = doc.selectFirst("meta[name=$metaName]") ?: continue
            val content = meta.attr("content")?.trim()
            if (!content.isNullOrEmpty() && content.length < AUTHOR_MAX_LENGTH) {
                return applyCleaners(content, cleaners)
            }
        }

        for (selector in cssSelectors) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (text.isNotEmpty() && text.length < AUTHOR_MAX_LENGTH) {
                return applyCleaners(text, cleaners)
            }
        }

        for ((selector, regex) in regexSelectors) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (regex.containsMatchIn(text)) {
                return applyCleaners(text, cleaners)
            }
        }

        return null
    }
}