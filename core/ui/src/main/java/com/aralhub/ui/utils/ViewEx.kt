package com.aralhub.ui.utils

import android.view.View
import androidx.core.view.isVisible

object ViewEx {
    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun View.enable() {
        isEnabled = true
    }

    fun View.disable() {
        isEnabled = false
    }

    fun <T : View, V> T.goneOrRun(value: V?, block: T.(V) -> Unit) {
        this.isVisible = value != null
        if (value != null) {
            this.block(value)
        }
    }
}