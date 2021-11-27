package hu.eqn34f.retroquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.eqn34f.retroquiz.databinding.ActivityDifficultySelectBinding
import hu.eqn34f.retroquiz.model.GameDifficulty
import hu.eqn34f.retroquiz.model.opentdb.Difficulty
import hu.eqn34f.retroquiz.utils.putExtra

class DifficultySelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDifficultySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()
    }


    private fun setButtonListeners() {
        binding.btnEasy.setOnClickListener { startActivity(getGameIntent(GameDifficulty.EASY)) }
        binding.btnNormal.setOnClickListener { startActivity(getGameIntent(GameDifficulty.NORMAL)) }
        binding.btnHard.setOnClickListener { startActivity(getGameIntent(GameDifficulty.HARD)) }
        binding.btnAdaptive.setOnClickListener { startActivity(getGameIntent(GameDifficulty.ADAPTIVE)) }
    }

    private fun getGameIntent(difficulty: GameDifficulty): Intent {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(difficulty)
        return intent
    }
}