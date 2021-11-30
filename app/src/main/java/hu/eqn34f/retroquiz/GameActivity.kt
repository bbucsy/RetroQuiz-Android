package hu.eqn34f.retroquiz

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import hu.eqn34f.retroquiz.data.HighScoreDatabase
import hu.eqn34f.retroquiz.data.OpenTdbRepository
import hu.eqn34f.retroquiz.data.model.GameDifficulty
import hu.eqn34f.retroquiz.data.model.HighScore
import hu.eqn34f.retroquiz.data.model.opentdb.Category
import hu.eqn34f.retroquiz.data.model.opentdb.Question
import hu.eqn34f.retroquiz.data.model.opentdb.QuestionType
import hu.eqn34f.retroquiz.databinding.ActivityGameBinding
import hu.eqn34f.retroquiz.fragment.AnswerDialogFragment
import hu.eqn34f.retroquiz.fragment.NetworkErrorDialogFragment
import hu.eqn34f.retroquiz.utils.PausableCountDownTimer
import hu.eqn34f.retroquiz.utils.getEnumExtra
import hu.eqn34f.retroquiz.utils.urlDecoded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity(), AnswerDialogFragment.AnswerDialogFragmentListener,
    NetworkErrorDialogFragment.NetworkErrorDialogListener {

    private lateinit var binding: ActivityGameBinding

    private lateinit var repository: OpenTdbRepository

    private lateinit var difficulty: GameDifficulty

    private lateinit var answerList: MutableList<Answer>

    private var playerScore: Int = 0

    private var level: Int = 0

    private lateinit var buttonList: List<Button>

    private lateinit var timer: PausableCountDownTimer

    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        difficulty = intent.getEnumExtra<GameDifficulty>() ?: GameDifficulty.ADAPTIVE
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        SetupRepository()
        SetUpButtonListeners()
        SetupTimer()
        NextQuestion()
    }

    private fun SetupRepository() {

        // general knowledge must be in the set, and it can't be disabled in the settings

        val allowedCategories = prefs.getStringSet("allowed_categories", mutableSetOf())
            ?.map { cat ->
                // use elvis operator and General knowledge for null safety
                Category.values().find { it.id.toString() == cat } ?: Category.GeneralKnowledge
            } ?: listOf<Category>()
            .toMutableList()
            .apply { add(Category.GeneralKnowledge) }
            .distinct()
            .toList()



        repository = OpenTdbRepository(difficulty, allowedCategories)
    }

    private fun SetupTimer() {
        val timeInMinutes = (prefs.getString("time_limit", "6")?.toIntOrNull()) ?: 6
        val timeFormat = resources.getString(R.string.time_format)
        timer = object : PausableCountDownTimer(timeInMinutes, 1000) {
            override fun onTick(p0: Long) {
                val totalSeconds = p0 / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                binding.txtTime.text = String.format(timeFormat, minutes, seconds)
            }

            override fun onFinish() {
                AnswerDialogFragment(AnswerDialogFragment.DialogState.TimeUp).show(
                    supportFragmentManager, AnswerDialogFragment.TAG
                )
            }
        }
    }


    private fun SetUpButtonListeners() {
        buttonList = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)

        buttonList.forEach {
            it.setOnClickListener { btn ->
                timer.pause()
                val answer = btn.tag as? Answer
                if (answer != null) {
                    if (answer.correct) {
                        playerScore += answer.question.points
                        AnswerDialogFragment(AnswerDialogFragment.DialogState.RightAnswer).show(
                            supportFragmentManager, AnswerDialogFragment.TAG
                        )
                    } else {
                        playerScore -= answer.question.points
                        AnswerDialogFragment(
                            AnswerDialogFragment.DialogState.WrongAnswer,
                            answer.question.correctAnswer.urlDecoded()
                        ).show(
                            supportFragmentManager, AnswerDialogFragment.TAG
                        )
                    }
                }
                // update score ui
                binding.txtScore.text = "Score:${playerScore}"
            }
        }
    }

    private fun ShowQuestion(question: Question) {
        // show question text
        binding.txtDifficulty.text = question.difficulty.name
        binding.txtLevel.text = level.toString()
        binding.txtQuestion.text = question.question.urlDecoded()

        //prepare answers
        answerList = mutableListOf(Answer(question.correctAnswer.urlDecoded(), true, question))
        answerList.addAll(question.incorrectAnswers.map {
            Answer(
                it.urlDecoded(),
                false,
                question
            )
        })
        answerList.shuffle()

        //show buttons
        binding.btnC.visibility =
            if (question.type == QuestionType.MultipleAnswer) View.VISIBLE else View.INVISIBLE
        binding.btnD.visibility =
            if (question.type == QuestionType.MultipleAnswer) View.VISIBLE else View.INVISIBLE

        //Set answers to buttons
        answerList.forEachIndexed { index, answer ->
            buttonList[index].text = answer.text
            buttonList[index].tag = answer
        }
        timer.resume()
    }

    private fun NextQuestion() {
        GlobalScope.launch(Dispatchers.Main) {
            // show loading screen
            timer.pause()
            binding.progressBar.visibility = View.VISIBLE
            buttonList.forEach { it.isEnabled = false }

            // get questions
            val questionResult = repository.getNextQuestion(playerScore)
            if (questionResult.isFailure) {
                NetworkErrorDialogFragment(questionResult.exceptionOrNull()).show(
                    supportFragmentManager, NetworkErrorDialogFragment.TAG
                )
            } else if (questionResult.isSuccess) {
                val question = questionResult.getOrNull()
                ShowQuestion(question!!)
            }


            //hide loading screen
            binding.progressBar.visibility = View.GONE
            buttonList.forEach { it.isEnabled = true }
        }
    }


    data class Answer(val text: String, val correct: Boolean, val question: Question)

    override fun onNextQuestion() {
        level++
        NextQuestion()
    }

    override fun onRetry() {
        NextQuestion()
    }

    override fun onFinishGame() {
        thread {
            val db = HighScoreDatabase.getDatabase(applicationContext).HighScoreDao()
            db.insert(
                HighScore(
                    name = prefs.getString("player_name", null) ?: "John Doe",
                    score = playerScore,
                    time = timer.timeInMinutes
                )
            )
            runOnUiThread {
                finish()
            }
        }
    }

}