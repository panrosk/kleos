package notiq.kleos

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import notiq.kleos.extractors.generic.AuthorExtractor
import notiq.kleos.extractors.generic.ContentExtractor
import notiq.kleos.extractors.generic.TitleExtractor

class ParserTest {

    private val parser = Parser()

    @Test
    fun `parse con HTML directo`() = runTest {
        val html = """
            <html>
                <head><title>Prueba</title></head>
                <body><h1>Hola mundo</h1></body>
            </html>
        """.trimIndent()

        val doc = parser.getDocument("https://example.com", html)

        println("Título: ${doc.selectFirst("title")?.text()}")
        println("H1: ${doc.selectFirst("h1")?.text()}")
        println("HTML completo:\n${doc.outerHtml()}")

        assertNotNull(doc.selectFirst("h1"))
    }

    @Test
    fun `fetch real desde as_com`() = runTest {
        val url = "https://as.com/"
        val doc = parser.getDocument(url)

        println("Título: ${doc.selectFirst("title")?.text()}")
        println("HTML truncado:\n${doc.outerHtml().take(1000)}...")

        assertNotNull(doc.selectFirst("title"))
    }

    @Test
    fun `url inválida lanza excepción`() = runTest {
        val url = "nota_url_valida"

        assertFailsWith<Exception> {
            parser.getDocument(url)
        }
    }

    @Test
    fun `extractor de autor sobre documento`() = runTest {
        val url = "https://marginalrevolution.com/marginalrevolution/2025/05/latin-america-is-swinging-to-the-traditional-non-trumpian-right.html"// Cambia si el contenido original cambia

        val extractor = AuthorExtractor()
        val extractor2 = TitleExtractor()
        val extractor3 = ContentExtractor()

        val doc = parser.getDocument(url)
        val author = parser.extract(doc, extractor)
        val title = parser.extract(doc, extractor2)
        val content = parser.extract(doc, extractor3)


        println("Autor extraído: $author")
        println("Title: $title")
        println("Content: $content")
        assertEquals("Tyler Cowen", author)
    }

}