package notiq.kleos.extractors.generic

import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import notiq.kleos.extractors.Extractor

private val DEFAULT_CONTENT_SELECTORS = listOf(
    ".article-body", ".entry-content", "article", ".post-content",
    ".post-body", ".content", "#article", "#content", "#main-content"
)

private const val DEFAULT_CONTENT_MIN_LENGTH = 200

class ContentExtractor(
    private val contentSelectors: List<String> = DEFAULT_CONTENT_SELECTORS,
    private val contentMinLength: Int = DEFAULT_CONTENT_MIN_LENGTH
) : Extractor {

    override fun extract(doc: Document): String? {
        for (selector in contentSelectors) {
            val container = doc.selectFirst(selector) ?: continue

            val markdown = container.children()
                .joinToString("\n\n") { convertToMarkdown(it).trim() }

            if (markdown.trim().length >= contentMinLength) {
                return markdown.trim()
            }
        }

        return null
    }

    private fun convertToMarkdown(element: Element): String {
        val childrenMarkdown = element.childNodes().joinToString("") { node ->
            when (node) {
                is Element -> convertToMarkdown(node)
                else -> node.outerHtml().trim()
            }
        }

        return when (element.tagName()) {
            "h1" -> "# $childrenMarkdown"
            "h2" -> "## $childrenMarkdown"
            "h3" -> "### $childrenMarkdown"
            "p" -> childrenMarkdown.trim()
            "blockquote" -> childrenMarkdown
                .lines()
                .joinToString("\n") { "> ${it.trim()}" }
            "pre" -> {
                val code = element.selectFirst("code")?.text()?.trim()
                    ?: childrenMarkdown.trim()
                "```\n$code\n```"
            }
            "ul" -> element.select("li")
                .joinToString("\n") { "- ${convertToMarkdown(it)}" }
            "ol" -> element.select("li")
                .mapIndexed { i, li -> "${i + 1}. ${convertToMarkdown(li)}" }
                .joinToString("\n")
            "code" -> "`$childrenMarkdown`"
            "a" -> {
                val href = element.attr("href").trim()
                val text = childrenMarkdown.trim()
                if (href.isNotEmpty()) "[$text]($href)" else text
            }
            "strong", "b" -> "**$childrenMarkdown**"
            "em", "i" -> "*$childrenMarkdown*"
            "img" -> {
                val src = element.attr("src").trim()
                val alt = element.attr("alt").trim()
                if (src.isNotEmpty()) "![${alt}]($src)" else ""
            }
            else -> childrenMarkdown
        }
    }
}