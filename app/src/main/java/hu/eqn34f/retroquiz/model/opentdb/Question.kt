package hu.eqn34f.retroquiz.model.opentdb

data class Question(
    val category : String,
    val type : String,
    val difficulty : Difficulty,
    val question : String,
    val correct_answer : String,
    val incorrect_answers : List<String>
)
