package com.aralhub.indrive.request.navigation

import androidx.navigation.NavController
import com.aralhub.indrive.request.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BottomSheetNavigator @Inject constructor(): FeatureRequestNavigation{
    private var navController : NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun goToSelectLocationFromRequestTaxi() {
        navController?.navigate(R.id.action_requestTaxiFragment_to_selectLocationFragment)
    }

    override fun goToSendOrderFromRequestTaxi() {
        navController?.navigate(R.id.action_requestTaxiFragment_to_sendOrderFragment)
    }
}