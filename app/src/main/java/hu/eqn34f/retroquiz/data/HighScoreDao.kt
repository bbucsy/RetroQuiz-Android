package hu.eqn34f.retroquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hu.eqn34f.retroquiz.data.model.HighScore

@Dao
interface HighScoreDao {
    @Insert
    fun insert(highScore: HighScore): Long

    @Query("SELECT * FROM highscore")
    fun getAll(): List<HighScore>

}