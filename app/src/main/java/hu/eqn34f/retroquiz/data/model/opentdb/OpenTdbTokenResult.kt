package hu.eqn34f.retroquiz.data.model.opentdb

import com.google.gson.annotations.SerializedName

data class OpenTdbTokenResult(
    @SerializedName("response_code")
    val responseCode: ResponseCode,

    @SerializedName("response_message")
    val responseMessage: String?,

    val token: String?
)
