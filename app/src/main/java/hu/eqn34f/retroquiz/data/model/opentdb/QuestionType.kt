package hu.eqn34f.retroquiz.data.model.opentdb

import com.google.gson.annotations.SerializedName

enum class QuestionType {
    @SerializedName("multiple")
    MultipleAnswer,

    @SerializedName("boolean")
    TrueOrFalse
}