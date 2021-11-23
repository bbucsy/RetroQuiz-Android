package hu.eqn34f.retroquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.eqn34f.retroquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}