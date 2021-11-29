package hu.eqn34f.retroquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import hu.eqn34f.retroquiz.databinding.ActivityGameBinding
import hu.eqn34f.retroquiz.fragment.AnswerDialogFragment
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Question
import hu.eqn34f.retroquiz.model.opentdb.QuestionType
import hu.eqn34f.retroquiz.repository.OpenTdbRepository
import hu.eqn34f.retroquiz.utils.getEnumExtra
import hu.eqn34f.retroquiz.utils.urlDecoded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GameActivity : AppCompatActivity(), AnswerDialogFragment.AnswerDialogFragmentListener {

    private lateinit var binding: ActivityGameBinding

    private lateinit var repository: OpenTdbRepository

    private lateinit var difficulty: GameDifficulty

    private lateinit var answerList: MutableList<Answer>

    private var playerScore: Int = 0

    // only for adaptive games
    private var levelScore: Int = 0

    private var level: Int = 0;

    private lateinit var buttonList: List<Button>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        difficulty = intent.getEnumExtra<GameDifficulty>() ?: GameDifficulty.ADAPTIVE

        repository = OpenTdbRepository(difficulty)
        setUpButtonListeners()
        NextQuestion()
    }


    private fun setUpButtonListeners() {
        buttonList = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)

        buttonList.forEach {
            it.setOnClickListener { btn ->
                val answer = btn.tag as? Answer
                if (answer != null) {
                    if (answer.correct) {
                        playerScore += answer.question.points
                        levelScore += answer.question.points
                        AnswerDialogFragment(AnswerDialogFragment.DialogState.RightAnswer).show(
                            supportFragmentManager, AnswerDialogFragment.TAG
                        )
                    } else {
                        levelScore -= answer.question.points
                        AnswerDialogFragment(AnswerDialogFragment.DialogState.WrongAnswer, answer.question.correctAnswer.urlDecoded()).show(
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
        binding.txtDifficulty.text = question.difficulty.name;
        binding.txtLevel.text = level.toString()
        binding.txtQuestion.text = question.question.urlDecoded();

        //prepare answers
        answerList = mutableListOf(Answer(question.correctAnswer.urlDecoded(), true, question))
        answerList.addAll(question.incorrectAnswers.map { Answer(it.urlDecoded(), false, question) })
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

    }

    private fun NextQuestion() {
        GlobalScope.launch(Dispatchers.Main) {
            // show loading screen
            level++
            binding.progressBar.visibility = View.VISIBLE
            buttonList.forEach { it.isEnabled = false }

            // get questions
            val questionResult = repository.getNextQuestion(levelScore)
            if (questionResult.isFailure) {
                Log.w("REPOSITORY", "Error with repository")
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
        NextQuestion()
    }

    override fun onExit() {
        finish()
    }
}