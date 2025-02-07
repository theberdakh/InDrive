package com.aralhub.indrive.ride.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.indrive.waiting.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SheetNavigator @Inject constructor(): FeatureRideBottomSheetNavigation {
    private var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun goToDriverIsWaiting() {
        navController?.navigate(R.id.driverIsWaitingBottomSheet)
    }

    override fun goToRide() {
        navController?.navigate(R.id.rideBottomSheet)
    }

    override fun goToRideFinished() {
        navController?.navigate(R.id.rideFinishedBottomSheet)
    }

    override fun goToRateDriverFromRideFinished() {
        navController?.navigate(R.id.action_rideFinishedBottomSheet_to_rateDriverBottomSheet)
    }

}