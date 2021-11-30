package hu.eqn34f.retroquiz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.eqn34f.retroquiz.R
import hu.eqn34f.retroquiz.data.model.HighScore
import hu.eqn34f.retroquiz.databinding.ItemHighScoreBinding

class HighScoreAdapter(private val context: Context) : RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder>() {

    private val scores = mutableListOf<HighScore>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder =
        HighScoreViewHolder(
            ItemHighScoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val item = scores[position]

        holder.binding.apply {
            txtName.text = item.name
            txtScore.text = item.score.toString()

            if(position == 0 || item.time != scores[position-1].time){
                header.visibility = View.VISIBLE
                headerText.text = String.format(context.getString(R.string.highscore_time_category_format),item.time)
            }
            else{
                header.visibility = View.GONE
            }
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun update(highscores: List<HighScore>) {
        scores.apply {
            clear()
            addAll(highscores)
            sortWith(compareBy<HighScore> {it.time} .thenByDescending { it.score } )
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = scores.size

    inner class HighScoreViewHolder(val binding: ItemHighScoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}