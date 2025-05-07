package notiq.kleos.extractors

import com.fleeksoft.ksoup.nodes.Document

public interface Extractor {
    fun extract(doc: Document): String?
}