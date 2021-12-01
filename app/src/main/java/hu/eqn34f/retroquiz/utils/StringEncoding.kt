package hu.eqn34f.retroquiz.utils

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// OpenTdb returns question with urlencoded strings, so this extension helps with that
fun String.urlDecoded(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}

