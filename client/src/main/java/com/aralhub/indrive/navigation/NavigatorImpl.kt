package com.aralhub.indrive.navigation

import androidx.navigation.NavController
import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.indrive.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(): Navigator, FeatureAuthNavigation {

    private var navController: NavController? = null

    override fun bind(navController: NavController){
        this.navController = navController
    }

    override fun unbind(){
        this.navController = null
    }

    override fun goToAddPhoneNumber() {
        navController?.navigate(R.id.action_logoFragment_to_addPhoneFragment)
    }

    override fun goToRequestTaxi() {
        navController?.navigate(R.id.action_logoFragment_to_requestTaxiFragment)
    }
}