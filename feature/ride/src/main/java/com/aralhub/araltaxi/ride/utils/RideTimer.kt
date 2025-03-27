package com.aralhub.araltaxi.ride.utils

import android.util.Log
import android.widget.TextView
import com.aralhub.araltaxi.client.ride.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToLong

class RideTimer {
    private var job: Job? = null
    private var isRideAccepted = false

    fun startTimer(startFreeTime: Double, endFreeTime: Double, textView: TextView) {
        job?.cancel() // Cancel any existing timer
        job = CoroutineScope(Dispatchers.Main).launch {
            // Phase 1: Countdown from start_free_time to end_free_time
            val freeDurationMillis = ((endFreeTime - startFreeTime) * 1000).roundToLong()
            val startMillis = (startFreeTime * 1000).roundToLong()
            val endMillis = (endFreeTime * 1000).roundToLong()

            while (System.currentTimeMillis() < endMillis && !isRideAccepted) {
                val timeLeftMillis = endMillis - System.currentTimeMillis()
                if (timeLeftMillis >= 0) {
                    val secondsLeft = (timeLeftMillis / 1000).toInt()
                    val minutes = secondsLeft / 60
                    val seconds = secondsLeft % 60
                    textView.text = "$minutes:${seconds.toString().padStart(2, '0')}"
                    println("Free time remaining: $minutes:${seconds.toString().padStart(2, '0')}")
                }
                delay(1000) // Update every second
            }

            // Phase 2: Paid waiting starts after end_free_time
            if (!isRideAccepted) {
                val paidWaitingStartMillis = (endFreeTime * 1000).roundToLong() // paidWaitingTime in milliseconds
                while (!isRideAccepted) {
                    val currentTimeMillis = System.currentTimeMillis()
                    val elapsedPaidMillis = currentTimeMillis - paidWaitingStartMillis
                    val elapsedPaidSeconds = (elapsedPaidMillis / 1000).toInt()

                    if (elapsedPaidSeconds >= 0) { // Ensure no negative values
                        val minutes = elapsedPaidSeconds / 60
                        val seconds = elapsedPaidSeconds % 60
                        textView.setTextColor(textView.context.getColor(com.aralhub.ui.R.color.color_status_error))
                        textView.text = "$minutes:${seconds.toString().padStart(2, '0')}"
                        println("Paid time: $minutes:${seconds.toString().padStart(2, '0')}")
                        Log.i("RideTimer", "Paid time: $minutes:${seconds.toString().padStart(2, '0')}")
                    }
                    delay(1000) // Update every second
                }
            }

            if (isRideAccepted) {
                println("Ride accepted, timer stopped.")
            }
        }
    }

    fun onRideAccepted() {
        isRideAccepted = true
    }

    fun stopTimer() {
        job?.cancel()
        isRideAccepted = false
    }
}

fun main() = runBlocking {
    val timer = RideTimer()

    // Example usage with your JSON data
    val startFreeTime = 1742882593.078141
    val endFreeTime = 1742882623.078147

   // timer.startTimer(startFreeTime, endFreeTime)

    // Simulate ride acceptance after some time (e.g., 35 seconds)
    delay(35000)
    timer.onRideAccepted()
}