package com.aralhub.araltaxi.ride.navigation.sheet

interface FeatureRideBottomSheetNavigation {
    fun goToWaitingForDriver()
    fun goToRideFromWaitingForDriver()
    fun goToRideFinishedFromWaitingForDriver()
    fun goToDriverIsWaiting()
    fun goToRide()
    fun goToRideFinished()
    fun goToRateDriverFromRideFinished()
}