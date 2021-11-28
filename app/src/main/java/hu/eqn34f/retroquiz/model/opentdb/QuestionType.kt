package hu.eqn34f.retroquiz.model.opentdb

import com.google.gson.annotations.SerializedName

enum class QuestionType {
    @SerializedName("multiple")
    MultipleAnswer,

    @SerializedName("boolean")
    TrueOrFalse
}