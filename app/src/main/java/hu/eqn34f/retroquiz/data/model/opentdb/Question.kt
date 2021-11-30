package hu.eqn34f.retroquiz.data.model.opentdb

import com.google.gson.annotations.SerializedName

data class Question(
    val category : Category,
    val type : QuestionType,
    val difficulty : Difficulty,
    val question : String,

    @SerializedName("correct_answer")
    val correctAnswer : String,

    @SerializedName("incorrect_answers")
    val incorrectAnswers : List<String>,
)
{
    val points: Int
        get() {
            return when(difficulty){
                Difficulty.NORMAL -> 3
                Difficulty.EASY -> 2
                Difficulty.HARD -> 5
            }
        }
}
