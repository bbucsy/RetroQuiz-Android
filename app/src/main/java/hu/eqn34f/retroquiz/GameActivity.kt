package hu.eqn34f.retroquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.eqn34f.retroquiz.databinding.ActivityGameBinding
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Question
import hu.eqn34f.retroquiz.repository.OpenTdbException
import hu.eqn34f.retroquiz.repository.OpenTdbRepository
import hu.eqn34f.retroquiz.utils.getEnumExtra
import hu.eqn34f.retroquiz.utils.urlDecoded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class GameActivity : AppCompatActivity() {


    private lateinit var binding: ActivityGameBinding

    private lateinit var repository: OpenTdbRepository

    private lateinit var difficulty: GameDifficulty

    private var playerScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        difficulty = intent.getEnumExtra<GameDifficulty>() ?: GameDifficulty.ADAPTIVE
        repository = OpenTdbRepository(difficulty)

    }


    private fun HandleError(exception: Throwable) {

    }



    private fun showQuestion(question: Question) {
        binding.txtDifficulty.text = question.difficulty.name;
        binding.txtLevel.text = "asd".urlDecoded()
        binding.txtQuestion.text = question.question.urlDecoded();
    }
}