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

    override fun goToRideFinishedBottomSheetFromRideBottomSheet() {
        navController?.navigate(R.id.action_rideBottomSheet_to_rideFinishedBottomSheet)
    }

}