package hu.eqn34f.retroquiz.data.model.opentdb

import com.google.gson.annotations.SerializedName

enum class Difficulty(val value: String) {
    @SerializedName("easy")
    EASY("easy"),

    @SerializedName("medium")
    NORMAL("medium"),

    @SerializedName("hard")
    HARD("hard")
}