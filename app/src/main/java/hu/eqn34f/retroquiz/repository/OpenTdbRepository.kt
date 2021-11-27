package hu.eqn34f.retroquiz.repository

import hu.eqn34f.retroquiz.data.OpenTdbNetwork
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Difficulty
import hu.eqn34f.retroquiz.model.opentdb.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class OpenTdbRepository constructor(
    private val difficulty: GameDifficulty
){

    private val api = OpenTdbNetwork.api
    private var sessionToken: String? = null;



    suspend fun refreshToken(){
        val result = api.getToken()
        sessionToken = result.token
    }

    suspend fun getNextQuestion(playerScore: Int): Question{
        return GlobalScope.async(Dispatchers.IO){
            if (sessionToken == null) refreshToken()
            val diff = getQuestionDifficulty(playerScore)
            val result = api.getQuestions( diff.value, sessionToken)
            return@async result.results.first()
        }.await();
    }


    private fun getQuestionDifficulty(playerScore: Int): Difficulty{
        return when(difficulty){
            GameDifficulty.EASY -> Difficulty.EASY
            GameDifficulty.NORMAL -> Difficulty.NORMAL
            GameDifficulty.HARD -> Difficulty.HARD
            GameDifficulty.ADAPTIVE -> {
                when {
                    playerScore <= 4 -> Difficulty.EASY
                    playerScore <= 12 -> Difficulty.NORMAL
                    else -> Difficulty.HARD
                }
            }
        }
    }

}