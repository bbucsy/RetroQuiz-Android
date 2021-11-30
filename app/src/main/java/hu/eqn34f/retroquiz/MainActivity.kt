package hu.eqn34f.retroquiz


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.eqn34f.retroquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners();
    }


    private fun setButtonListeners() {
        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, DifficultySelectActivity::class.java))
        }
        binding.btnOpions.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnHighscore.setOnClickListener {
            startActivity(Intent(this, HighScoreActivity::class.java))
        }
    }
}