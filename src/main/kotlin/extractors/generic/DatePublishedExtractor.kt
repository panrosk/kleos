package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor
import notiq.kleos.cleaners.Cleaner
import notiq.kleos.cleaners.applyCleaners

private const val DATE_MAX_LENGTH = 100

private val DEFAULT_META_TAGS = listOf(
    "article:published_time", "displaydate", "dc.date", "dc.date.issued",
    "rbpubdate", "publish_date", "pub_date", "pagedate", "pubdate",
    "revision_date", "doc_date", "date_created", "content_create_date",
    "lastmodified", "created", "date"
)

private val DEFAULT_SELECTORS = listOf(
    ".hentry .dtstamp.published", ".hentry .published", ".hentry .dtstamp.updated",
    ".hentry .updated", ".single .published", ".meta .published", ".meta .postDate",
    ".entry-date", ".byline .date", ".postmetadata .date", ".article_datetime",
    ".date-header", ".story-date", ".dateStamp", "#story .datetime", ".dateline",
    ".pubdate"
)

private val ABBREV_MONTHS = "(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)"

private val DEFAULT_URL_REGEX = listOf(
    Regex("/(20\\d{2}/\\d{2}/\\d{2})/", RegexOption.IGNORE_CASE),
    Regex("(20\\d{2}-[01]\\d-[0-3]\\d)", RegexOption.IGNORE_CASE),
    Regex("/(20\\d{2}/$ABBREV_MONTHS/[0-3]\\d)/", RegexOption.IGNORE_CASE)
)

class DatePublishedExtractor(
    private val metaTags: List<String> = DEFAULT_META_TAGS,
    private val cssSelectors: List<String> = DEFAULT_SELECTORS,
    private val urlPatterns: List<Regex> = DEFAULT_URL_REGEX,
    private val cleaners: List<Cleaner> = emptyList()
) : Extractor {

    override fun extract(doc: Document): String? {
        val url = doc.baseUri()

        for (metaName in metaTags) {
            val meta = doc.selectFirst("""meta[property="$metaName"], meta[name="$metaName"]""") ?: continue
            val content = meta.attr("content").trim()
            if (content.isNotEmpty() && content.length <= DATE_MAX_LENGTH) {
                return applyCleaners(content, cleaners)
            }
        }

        for (selector in cssSelectors) {
            val element = doc.selectFirst(selector) ?: continue
            val text = element.text().trim()
            if (text.isNotEmpty() && text.length <= DATE_MAX_LENGTH) {
                return applyCleaners(text, cleaners)
            }
        }

        for (regex in urlPatterns) {
            val match = regex.find(url)
            if (match != null && match.groupValues.size > 1) {
                val dateString = match.groupValues[1].trim()
                if (dateString.isNotEmpty() && dateString.length <= DATE_MAX_LENGTH) {
                    return applyCleaners(dateString, cleaners)
                }
            }
        }

        return null
    }
}