package com.aralhub.offers.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.araltaxi.client.offers.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SheetNavigatorImpl @Inject constructor(): SheetNavigator, FeatureOffersBottomSheetNavigation {
    private var navController : NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun goToChangePriceFragment() {
        navController?.navigate(R.id.action_changeOrCancelRequestBottomSheet_to_changePriceBottomSheet)
    }
}