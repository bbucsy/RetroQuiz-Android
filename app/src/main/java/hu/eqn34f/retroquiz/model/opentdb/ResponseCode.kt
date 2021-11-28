package hu.eqn34f.retroquiz.model.opentdb

import com.google.gson.annotations.SerializedName

enum class ResponseCode {
    @SerializedName("0")
    Success,

    @SerializedName("1")
    NoResult,

    @SerializedName("2")
    InvalidParameter,

    @SerializedName("3")
    TokenNotFound,

    @SerializedName("4")
    TokenEmpty
}