package com.aralhub.indrive.ride

import android.os.CountDownTimer

class WaitingCountDownTimer(
    private val onCountDownTimerTick: (minutes: Long, seconds: Long) -> Unit,
    private val onCountDownTimerFinish: () -> Unit
): CountDownTimer(120000, 1000) {
    override fun onTick(millisUntilFinished: Long) {
        val minutes = millisUntilFinished / 60000
        val seconds = (millisUntilFinished % 60000) / 1000
        onCountDownTimerTick(minutes, seconds)
    }

    override fun onFinish() {
        onCountDownTimerFinish()
    }
}