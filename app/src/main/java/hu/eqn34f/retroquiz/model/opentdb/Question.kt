package hu.eqn34f.retroquiz.model.opentdb

import com.google.gson.annotations.SerializedName

data class Question(
    val category : Category,
    val type : QuestionType,
    val difficulty : Difficulty,
    val question : String,

    @SerializedName("correct_answer")
    val correctAnswer : String,

    @SerializedName("incorrect_answers")
    val incorrectAnswers : List<String>
)
