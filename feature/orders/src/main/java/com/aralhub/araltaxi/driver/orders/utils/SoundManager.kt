package com.aralhub.araltaxi.driver.orders.utils

import android.content.Context
import android.media.SoundPool
import com.aralhub.araltaxi.driver.orders.R

class SoundManager(context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .build()
    
    private val soundId: Int = soundPool.load(context, R.raw.sound_new_order, 1)

    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
