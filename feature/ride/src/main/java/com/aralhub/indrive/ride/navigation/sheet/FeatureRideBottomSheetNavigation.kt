package com.aralhub.indrive.ride.navigation.sheet

interface FeatureRideBottomSheetNavigation {
    fun goToWaitingForDriver()
    fun goToDriverIsWaiting()
    fun goToRide()
    fun goToRideFinished()
    fun goToRateDriverFromRideFinished()
}