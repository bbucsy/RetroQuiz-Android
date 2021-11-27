package hu.eqn34f.retroquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.eqn34f.retroquiz.databinding.ActivityDifficultySelectBinding

class DifficultySelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDifficultySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}