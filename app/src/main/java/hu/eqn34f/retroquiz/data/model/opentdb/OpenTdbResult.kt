package hu.eqn34f.retroquiz.data.model.opentdb

import com.google.gson.annotations.SerializedName

data class OpenTdbResult(
    @SerializedName("response_code")
    val responseCode: ResponseCode,

    val results: List<Question>
)
