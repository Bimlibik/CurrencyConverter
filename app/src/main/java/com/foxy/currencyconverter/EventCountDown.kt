package com.foxy.currencyconverter

import android.os.CountDownTimer

/**
*  Timer that on finish updates data from network.
 */
class EventCountDown(val listener: TimerListener) {

    private var millisInFuture: Long = 10000L
    private val countDownInterval: Long = 1000L
    private var millisRemaining: Long = 0L

    private lateinit var timer: CountDownTimer

    fun start() {
        millisRemaining = millisInFuture

        if (this::timer.isInitialized) {
            timer.start()
        } else {
            createTimer()
            timer.start()
        }
    }

    private fun createTimer() {
        timer = object : CountDownTimer(millisRemaining, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                listener.onTick(millisUntilFinished / countDownInterval)
            }

            override fun onFinish() {
                listener.onFinish()
                timer.start()
            }
        }
    }

}


interface TimerListener {

    fun onTick(time: Long)

    fun onFinish()

}