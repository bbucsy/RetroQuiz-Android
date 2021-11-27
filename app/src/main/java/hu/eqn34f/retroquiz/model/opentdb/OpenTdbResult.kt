package hu.eqn34f.retroquiz.model.opentdb

data class OpenTdbResult(
    val response_code : Int,
    val results : List<Question>
)
