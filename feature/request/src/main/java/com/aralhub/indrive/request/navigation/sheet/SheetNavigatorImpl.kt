package com.aralhub.indrive.request.navigation.sheet

import androidx.navigation.NavController
import com.aralhub.indrive.request.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SheetNavigatorImpl @Inject constructor(): SheetNavigator,
    FeatureRequestBottomSheetNavigation {
    private var navController : NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun goToSelectLocationFromRequestTaxi() {
        navController?.navigate(R.id.action_requestTaxiBottomSheet_to_selectLocationBottomSheet)
    }

    override fun goToSendOrderFromRequestTaxi() {
        navController?.navigate(R.id.action_requestTaxiBottomSheet_to_sendOrderBottomSheet)
    }

    override fun goBackToRequestTaxiFromSendOrder() {
        navController?.navigateUp()
    }
}