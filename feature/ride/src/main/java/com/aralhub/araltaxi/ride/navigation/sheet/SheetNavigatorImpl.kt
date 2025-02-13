package com.aralhub.araltaxi.ride.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.indrive.waiting.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SheetNavigatorImpl @Inject constructor() : SheetNavigator,
    FeatureRideBottomSheetNavigation {
    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun goToWaitingForDriver() {
        navController?.navigate(R.id.waitingForDriverBottomSheet)
    }

    override fun goToDriverIsWaiting() {
        navController?.navigate(R.id.action_waitingForDriverBottomSheet_toDriverIsWaitingBottomSheet)
    }

    override fun goToRide() {
        navController?.navigate(R.id.action_driverIsWaitingBottomSheet_toRideBottomSheet)
    }

    override fun goToRideFinished() {
        navController?.navigate(R.id.action_rideBottomSheet_to_rideFinishedBottomSheet)
    }

    override fun goToRateDriverFromRideFinished() {
        navController?.navigate(R.id.action_rideFinishedBottomSheet_to_rateDriverBottomSheet)
    }

}