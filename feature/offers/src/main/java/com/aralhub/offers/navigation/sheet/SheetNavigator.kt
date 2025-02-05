package com.aralhub.offers.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.offers.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SheetNavigator @Inject constructor(): FeatureOffersBottomSheetNavigation {
    private var navController : NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun goToChangePriceFragment() {
        navController?.navigate(R.id.action_fragmentChangeOrCancelRequest_to_changePriceFragment)
    }
}