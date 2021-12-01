package hu.eqn34f.retroquiz.data

import hu.eqn34f.retroquiz.data.model.GameDifficulty
import hu.eqn34f.retroquiz.data.model.opentdb.Category
import hu.eqn34f.retroquiz.data.model.opentdb.Difficulty
import hu.eqn34f.retroquiz.data.model.opentdb.Question
import hu.eqn34f.retroquiz.data.model.opentdb.ResponseCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class OpenTdbRepository constructor(
    private val difficulty: GameDifficulty,
    private val allowedCategories: List<Category>
) {

    private val api = OpenTdbNetwork.api
    private var sessionToken: String? = null
    private var cacheAmount = 10
    private val cache = mutableSetOf<Question>()

    private suspend fun refreshToken() {
        val result = api.getToken()
        sessionToken = result.token
    }

    private fun getFromCache(diff: Difficulty, category: Category): Question? {
        return cache
            .filter { it.category == category && it.difficulty == diff }
            .randomOrNull()
            ?.also {
                cache.remove(it)
            }
    }

    private fun addToCache(results: List<Question>, blocked: Question) {
        cache.addAll(results)
        cache.remove(blocked)
    }

    private fun getRandomCategory(): Category {
        return if (allowedCategories.isEmpty())
            Category.GeneralKnowledge
        else
            allowedCategories.random()
    }

    suspend fun getNextQuestion(playerScore: Int): Result<Question> = kotlin.runCatching {
        GlobalScope.async(Dispatchers.IO) {
            //refresh token if there isn't one set
            if (sessionToken == null) refreshToken()


            val diff = getQuestionDifficulty(playerScore)
            val category = getRandomCategory()

            val cachedQuestion = getFromCache(diff, category)
            if (cachedQuestion != null) return@async cachedQuestion

            //get result
            val result = api.getQuestions(
                amount = cacheAmount,
                difficulty = diff.value,
                category = category.id,
                token = sessionToken
            )

            if (result.responseCode == ResponseCode.Success && result.results.isNotEmpty()) {
                // request is successful, return question
                val question = result.results.first()
                addToCache(result.results, question)
                return@async question
            } else if (result.responseCode == ResponseCode.NoResult) {
                //there might be not enough question, turn off cache to see if problem persists
                cacheAmount = 1
                throw Exception("Error getting enough questions. Try again")
            } else if (result.responseCode == ResponseCode.TokenNotFound || result.responseCode == ResponseCode.TokenEmpty) {
                // Something went wrong with token, it expired or all questions where asked
                // client should try again after renewing the token
                refreshToken()
                throw Exception("OpenTdb token error")
            } else
                throw Exception("Error getting question from db")

        }.await()
    }

    private fun getQuestionDifficulty(playerScore: Int): Difficulty {
        return when (difficulty) {
            GameDifficulty.EASY -> Difficulty.EASY
            GameDifficulty.NORMAL -> Difficulty.NORMAL
            GameDifficulty.HARD -> Difficulty.HARD
            GameDifficulty.ADAPTIVE -> {
                when {
                    playerScore <= 4 -> Difficulty.EASY
                    playerScore <= 16 -> Difficulty.NORMAL
                    else -> Difficulty.HARD
                }
            }
        }
    }

}