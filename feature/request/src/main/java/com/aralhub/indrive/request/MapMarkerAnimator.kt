package com.aralhub.indrive.request

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
/*

class MapMarkerAnimator(private val context: Context) {
    private var markerView: View? = null
    private var markerPin: ImageView? = null
    private var markerShadow: ImageView? = null
    private var isMarkerRaised = false

    fun initMarker(mapView: ViewGroup) {
        mapView.clipChildren = false
        mapView.clipToPadding = false
        markerView = LayoutInflater.from(context).inflate(R.layout.layout_location_icon, mapView, false)
        markerPin = markerView?.findViewById(R.id.marker_pin)
        markerShadow = markerView?.findViewById(R.id.marker_shadow)
        // Устанавливаем высокий z-index для всего маркера
        markerView?.elevation = 1000f
        // И еще выше для самого пина
        markerPin?.elevation = 1001f
        mapView.addView(markerView)

        markerView?.post {
            markerView?.x = (mapView.width - markerView?.width!!) / 2f
            markerView?.y = (mapView.height - markerView?.height!!) / 2f
        }
    }

    fun hideMarkerView() {
        markerView?.isVisible = false
    }

    fun onMapCameraMoveStarted() {
        if (!isMarkerRaised) {
            animateMarkerUp()
        }
    }

    fun onMapCameraIdle() {
        if (isMarkerRaised) {
            animateMarkerDown()
        }
    }

    private fun animateMarkerUp() {
        isMarkerRaised = true

        // Анимация поднятия маркера
        val pinUpAnimation = ObjectAnimator.ofFloat(markerPin, "translationY", 0f, -40f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Анимация уменьшения тени
        val shadowAnimation = ObjectAnimator.ofPropertyValuesHolder(
            markerShadow,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f),
            PropertyValuesHolder.ofFloat("alpha", 0.3f, 0.15f)
        ).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(pinUpAnimation, shadowAnimation)
            start()
        }
    }

    private fun animateMarkerDown() {
        isMarkerRaised = false

        // Анимация опускания маркера
        val pinDownAnimation = ObjectAnimator.ofFloat(markerPin, "translationY", -40f, 0f).apply {
            duration = 300
            interpolator = OvershootInterpolator(1.2f)
        }

        // Анимация возврата тени
        val shadowAnimation = ObjectAnimator.ofPropertyValuesHolder(
            markerShadow,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f),
            PropertyValuesHolder.ofFloat("alpha", 0.15f, 0.3f)
        ).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }

        AnimatorSet().apply {
            playTogether(pinDownAnimation, shadowAnimation)
            start()
        }
    }
}*/
