package hu.eqn34f.retroquiz.model.opentdb

data class OpenTdbTokenResult(
    val response_code: Int,
    val respone_message: String?,
    val token: String?
)
