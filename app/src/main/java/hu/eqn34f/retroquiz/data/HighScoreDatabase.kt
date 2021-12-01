package hu.eqn34f.retroquiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.eqn34f.retroquiz.data.model.HighScore

@Database(entities = [HighScore::class], version = 1)
abstract class HighScoreDatabase : RoomDatabase() {

    abstract fun HighScoreDao(): HighScoreDao

    companion object {
        fun getDatabase(applicationContext: Context): HighScoreDatabase {
            return Room.databaseBuilder(
                applicationContext,
                HighScoreDatabase::class.java,
                "highscore"
            ).build()
        }
    }

}