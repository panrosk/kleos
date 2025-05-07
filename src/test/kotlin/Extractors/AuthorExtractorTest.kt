package notiq.kleos.extractors.generic.author

import com.fleeksoft.ksoup.Ksoup
import notiq.kleos.extractors.generic.AuthorExtractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthorExtractorTest {

    private val extractor = AuthorExtractor()

    @Test
    fun testExtractFromMetaTag() {
        val html = """
            <html>
              <head>
                <meta name="dc.author" content="By John Doe" />
              </head>
              <body></body>
            </html>
        """.trimIndent()

        val doc = Ksoup.parse(html)
        val result = extractor.extract(doc)
        assertEquals("John Doe", result)
    }

    @Test
    fun testExtractFromSelector() {
        val html = """
            <html>
              <body>
                <div class="author">by Jane Smith</div>
              </body>
            </html>
        """.trimIndent()

        val doc = Ksoup.parse(html)
        val result = extractor.extract(doc)
        assertEquals("Jane Smith", result)
    }

    @Test
    fun testExtractFromBylineRegex() {
        val html = """
            <html>
              <body>
                <div id="byline">By Alex Johnson</div>
              </body>
            </html>
        """.trimIndent()

        val doc = Ksoup.parse(html)
        val result = extractor.extract(doc)
        assertEquals("Alex Johnson", result)
    }

    @Test
    fun testReturnsNullWhenNoAuthorFound() {
        val html = """
            <html>
              <body>
                <p>No author info here</p>
              </body>
            </html>
        """.trimIndent()

        val doc = Ksoup.parse(html)
        val result = extractor.extract(doc)
        assertNull(result)
    }

    @Test
    fun testIgnoresLongAuthorName() {
        val longAuthor = "A".repeat(350)
        val html = """
            <html>
              <head>
                <meta name="dc.author" content="$longAuthor" />
              </head>
            </html>
        """.trimIndent()

        val doc = Ksoup.parse(html)
        val result = extractor.extract(doc)
        assertNull(result)
    }
}