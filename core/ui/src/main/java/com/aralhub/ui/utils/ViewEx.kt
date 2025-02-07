package com.aralhub.ui.utils

import android.view.View

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
}