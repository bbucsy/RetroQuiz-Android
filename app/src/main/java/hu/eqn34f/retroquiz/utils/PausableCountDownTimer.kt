package hu.eqn34f.retroquiz.utils

import android.os.CountDownTimer


//wraps a CountDown timer, and keeps track of its passage
//lets the user pause and resume the timer
abstract class PausableCountDownTimer(
    val timeInMinutes: Int, val countDownInterval: Long = 1000,
) {

    private var remaining = (timeInMinutes * 60000).toLong()
    private var isPaused = false
    private var timer: CountDownTimer

    init {
        timer = createTimer(remaining).start()
    }


    //creates a timer with the remaining time as starting value
    private fun createTimer(start: Long): CountDownTimer {
        return object : CountDownTimer(start, countDownInterval) {
            override fun onTick(p0: Long) {
                remaining = p0
                this@PausableCountDownTimer.onTick(p0)
            }

            override fun onFinish() {
                this@PausableCountDownTimer.onFinish()
            }
        }
    }

    abstract fun onTick(p0: Long)
    abstract fun onFinish()


    fun pause() {
        if (!isPaused && remaining > 0) {
            timer.cancel()
            isPaused = true
        }
    }

    fun resume() {
        if (isPaused && remaining > 0) {
            timer = createTimer(remaining).start()
            isPaused = false
        }
    }

}