package hu.eqn34f.retroquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.eqn34f.retroquiz.databinding.ActivityGameBinding
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Question
import hu.eqn34f.retroquiz.repository.OpenTdbRepository
import hu.eqn34f.retroquiz.utils.urlDecoded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var repository: OpenTdbRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = OpenTdbRepository(GameDifficulty.NORMAL)

        getNextQuestion()
    }


    private fun getNextQuestion(){
        GlobalScope.launch(Dispatchers.Main) {
            val question = repository.getNextQuestion(0)
            showQuestion(question)
        }
    }

    private fun showQuestion(question: Question){
        binding.txtDifficulty.text = question.difficulty.name;
        binding.txtLevel.text = "asd".urlDecoded()
        binding.txtQuestion.text = question.question.urlDecoded();
    }
}