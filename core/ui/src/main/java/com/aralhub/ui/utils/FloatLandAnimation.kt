package com.aralhub.ui.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator

class FloatLandAnimation(private val centerMarker: View) {

    private var floatingAnimatorSet: AnimatorSet? = null
    private var landingAnimatorSet: AnimatorSet? = null

    fun startFloating() {
        // Cancel any existing animations
        stopAnimations()

        // Create floating animation
        val translateY = ObjectAnimator.ofFloat(centerMarker, "translationY", -20f, 0f, -10f, 0f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleX = ObjectAnimator.ofFloat(centerMarker, "scaleX", 0.95f, 1f, 0.98f, 1f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        val scaleY = ObjectAnimator.ofFloat(centerMarker, "scaleY", 0.95f, 1f, 0.98f, 1f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }

        floatingAnimatorSet = AnimatorSet().apply {
            playTogether(translateY, scaleX, scaleY)
            start()
        }
    }

    fun land() {
        // Cancel floating animation
        floatingAnimatorSet?.cancel()

        // Create landing animation
        val translationYAnimator = ObjectAnimator.ofFloat(centerMarker, "translationY", centerMarker.translationY, 0f).apply {
            duration = 500
            interpolator = BounceInterpolator()
        }

        val scaleXAnimator = ObjectAnimator.ofFloat(centerMarker, "scaleX", centerMarker.scaleX, 1f).apply {
            duration = 500
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(centerMarker, "scaleY", centerMarker.scaleY, 1f).apply {
            duration = 500
        }

        landingAnimatorSet = AnimatorSet().apply {
            playTogether(translationYAnimator, scaleXAnimator, scaleYAnimator)
            start()
        }
    }

    fun stopAnimations() {
        floatingAnimatorSet?.cancel()
        landingAnimatorSet?.cancel()

        // Reset to default state
        centerMarker.translationY = 0f
        centerMarker.scaleX = 1f
        centerMarker.scaleY = 1f
    }
}