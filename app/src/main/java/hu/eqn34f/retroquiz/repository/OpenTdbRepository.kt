package hu.eqn34f.retroquiz.repository

import hu.eqn34f.retroquiz.data.OpenTdbNetwork
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Difficulty
import hu.eqn34f.retroquiz.model.opentdb.Question
import hu.eqn34f.retroquiz.model.opentdb.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.Exception
import java.util.*

class OpenTdbException(val responseCode: ResponseCode, val shouldRetry: Boolean = false) : Exception("OpenTdb throwm error")

class OpenTdbRepository constructor(
    private val difficulty: GameDifficulty
) {

    private val api = OpenTdbNetwork.api
    private var sessionToken: String? = null;

    companion object {
        val REQUEST_TRIES = 3
    }


    suspend fun refreshToken() {
        val result = api.getToken()
        sessionToken = result.token
    }


    suspend fun getNextQuestion(playerScore: Int): Result<Question> = kotlin.runCatching {
        GlobalScope.async(Dispatchers.IO) {
            if (sessionToken == null) refreshToken()

            //get result
            val result = api.getQuestions(getQuestionDifficulty(playerScore).value, sessionToken)

            if (result.responseCode == ResponseCode.Success && result.results.isNotEmpty())
            // request is successful, return questiom
                return@async result.results.first()
            else if (result.responseCode == ResponseCode.TokenNotFound || result.responseCode == ResponseCode.TokenEmpty) {
                // Something went wrong with token, it expired or all questions where asked
                // client should try again after renewing the token
                refreshToken()
                throw OpenTdbException(result.responseCode, shouldRetry = true)
            } else
                throw OpenTdbException(result.responseCode)

        }.await();
    }


    private fun getQuestionDifficulty(playerScore: Int): Difficulty {
        return when (difficulty) {
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