package com.aralhub.home.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.indrive.request.navigation.FeatureRequestNavigation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SheetNavigator @Inject constructor(): FeatureRequestNavigation {
    private var navController : NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun goToSelectLocationFromRequestTaxi() {
        navController?.navigate(com.aralhub.home.R.id.action_requestTaxiFragment_to_selectLocationFragment)
    }

    override fun goToSendOrderFromRequestTaxi() {
        navController?.navigate(com.aralhub.home.R.id.action_requestTaxiFragment_to_sendOrderFragment)
    }
}