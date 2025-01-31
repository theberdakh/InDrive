package com.aralhub.indrive.navigation

import androidx.navigation.NavController
import com.aralhub.auth.AddSMSFragment
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

    override fun goToRequestTaxiFromLogo() {
        navController?.navigate(R.id.action_logoFragment_to_requestTaxiFragment)
    }

    override fun goToRequestTaxiFromAddName() {
        navController?.navigate(R.id.action_addNameFragment_to_requestTaxiFragment)
    }

    override fun goToAddSMSCode(phone: String) {
        navController?.navigate(R.id.action_addPhoneFragment_to_addSmsFragment,  AddSMSFragment.args(phone))
    }

    override fun goToAddName() {
        navController?.navigate(R.id.action_addSmsFragment_to_addNameFragment)
    }
}