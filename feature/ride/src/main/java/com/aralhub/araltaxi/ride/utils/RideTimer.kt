package com.aralhub.araltaxi.ride.utils

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
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
                var paidSeconds = 0
                while (!isRideAccepted) {
                    paidSeconds++
                    val minutes = paidSeconds / 60
                    val seconds = paidSeconds % 60
                    textView.text = "$minutes:${seconds.toString().padStart(2, '0')}"
                    println("Paid time: $minutes:${seconds.toString().padStart(2, '0')}")
                    Log.i("RideTimer", "Paid time: $minutes:${seconds.toString().padStart(2, '0')}")
                    delay(1000)
                }
            }

            if (isRideAccepted) {
                println("Ride accepted, timer stopped.")
            }
        }
    }

    // Call this when "ride_accepted" status is received
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