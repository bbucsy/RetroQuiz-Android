package hu.eqn34f.retroquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.eqn34f.retroquiz.adapter.HighScoreAdapter
import hu.eqn34f.retroquiz.data.HighScoreDatabase
import hu.eqn34f.retroquiz.databinding.ActivityHighScoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class HighScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHighScoreBinding
    private lateinit var database: HighScoreDatabase
    private lateinit var adapter: HighScoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = HighScoreDatabase.getDatabase(applicationContext)

        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = HighScoreAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.HighScoreDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

}