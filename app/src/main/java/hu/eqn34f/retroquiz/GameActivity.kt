package hu.eqn34f.retroquiz

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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

    //This is attached to a button, so the listener knows if the answer was right
    data class Answer(val text: String, val correct: Boolean, val question: Question)

    private lateinit var binding: ActivityGameBinding

    private lateinit var repository: OpenTdbRepository

    private lateinit var difficulty: GameDifficulty

    private lateinit var answerList: MutableList<Answer>

    private var playerScore: Int = 0

    private var questionNumber: Int = 1

    private lateinit var buttonList: List<Button>

    private lateinit var timer: PausableCountDownTimer

    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        difficulty = intent.getEnumExtra<GameDifficulty>() ?: GameDifficulty.ADAPTIVE
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        binding.txtScore.text = String.format(getString(R.string.game_score_format), playerScore)

        setupRepository()
        setUpButtonListeners()
        setupTimer()
        nextQuestion()
    }

    //set up question repository with the allowed categories and difficulty setting
    private fun setupRepository() {

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

    // create timer for game
    private fun setupTimer() {
        // get time limit from settings
        val timeInMinutes = (prefs.getString("time_limit", "6")?.toIntOrNull()) ?: 6
        val timeFormat = resources.getString(R.string.time_format)

        // create a timer object and override the onTick and on Finish functions to display time
        timer = object : PausableCountDownTimer(timeInMinutes, 1000) {
            override fun onTick(p0: Long) {
                val totalSeconds = p0 / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                // display the time in the specified format
                binding.txtTime.text = String.format(timeFormat, minutes, seconds)
            }

            override fun onFinish() {
                // show end game dialog
                AnswerDialogFragment(AnswerDialogFragment.DialogState.TimeUp).show(
                    supportFragmentManager, AnswerDialogFragment.TAG
                )
            }
        }
    }


    private fun setUpButtonListeners() {
        buttonList = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)

        buttonList.forEach {
            it.setOnClickListener { btn ->
                // pause timer, so its not ticking while showing the answer dialog
                timer.pause()

                //get answer from tag
                val answer = btn.tag as? Answer
                if (answer != null) {
                    if (answer.correct) {
                        // correct answer, add points and show dialog
                        playerScore += answer.question.points
                        AnswerDialogFragment(AnswerDialogFragment.DialogState.RightAnswer).show(
                            supportFragmentManager, AnswerDialogFragment.TAG
                        )
                    } else {
                        // incorrect answer, remove points and show dialog
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
                binding.txtScore.text =
                    String.format(getString(R.string.game_score_format), playerScore)
            }
        }
    }

    private fun showQuestion(question: Question) {
        // show question text
        binding.txtDifficulty.text = question.difficulty.name
        binding.txtLevel.text =
            String.format(resources.getString(R.string.level_text_format), questionNumber)
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
        //shuffle the answers
        answerList.shuffle()

        //hide last two buttons if question is not multiple choice
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

    private fun nextQuestion() {
        GlobalScope.launch(Dispatchers.Main) {
            // show loading screen
            timer.pause()
            binding.progressBar.visibility = View.VISIBLE
            buttonList.forEach { it.isEnabled = false }

            // get questions
            val questionResult = repository.getNextQuestion(playerScore)
            if (questionResult.isSuccess) {
                //show question
                val question = questionResult.getOrNull()
                showQuestion(question!!)
            } else {
                //show error dialog
                NetworkErrorDialogFragment(questionResult.exceptionOrNull()).show(
                    supportFragmentManager, NetworkErrorDialogFragment.TAG
                )
            }


            //hide loading screen
            binding.progressBar.visibility = View.GONE
            buttonList.forEach { it.isEnabled = true }
        }
    }



    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.exit_confirm_message))
            .setPositiveButton(getString(R.string.exit_confirm_ok)) { _, _ -> onFinishGame() }
            .setNegativeButton(getString(R.string.exit_confirm_cancel), null)
            .show()
    }

    override fun onNextQuestion() {
        questionNumber++
        nextQuestion()
    }

    override fun onRetry() {
        nextQuestion()
    }

    override fun onFinishGame() {
        //save score in db
        thread {
            val db = HighScoreDatabase.getDatabase(applicationContext).HighScoreDao()
            db.insert(
                HighScore(
                    name = prefs.getString("player_name", null) ?: getString(R.string.default_name),
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