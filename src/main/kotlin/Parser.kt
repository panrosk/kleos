package notiq.kleos

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import notiq.kleos.extractors.Extractor
import java.net.MalformedURLException
import java.net.URL

class Parser()  {
    fun  extract(document: Document,extractor: Extractor): String {
        val extracted = extractor.extract(document)

        if (extracted==null)
            return ""

        return extracted
    }

    public suspend fun getDocument(url: String, html: String? = null): Document {
        validateUrl(url)
        return html?.let { Ksoup.parse(it) } ?: fetchFromUrl(url)
    }

    private suspend fun fetchFromUrl(url: String): Document {
        return Ksoup.parseGetRequest(url)
    }

    private fun validateUrl(url: String) {
        try {
            URL(url)
        } catch (e: MalformedURLException) {
            throw MalformedURLException("URL no válida: $url")
        }
    }
}