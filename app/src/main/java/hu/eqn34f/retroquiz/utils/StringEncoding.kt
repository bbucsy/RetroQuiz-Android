package hu.eqn34f.retroquiz.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


fun String.urlEncoded() : String{
    return URLEncoder.encode(this,StandardCharsets.UTF_8.toString())
}

fun String.urlDecoded(): String{
    return URLDecoder.decode(this,StandardCharsets.UTF_8.toString())
}

