package com.aralhub.indrive.navigation

import androidx.navigation.NavController
import com.aralhub.auth.AddSMSFragment
import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.home.navigation.FeatureHomeNavigation
import com.aralhub.indrive.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(): Navigator, FeatureAuthNavigation, FeatureHomeNavigation {

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

    override fun goToHomeFragmentFromAddName() {
        navController?.navigate(R.id.action_addNameFragment_to_HomeFragment)
    }

    override fun goToAddSMSCode(phone: String) {
        navController?.navigate(R.id.action_addPhoneFragment_to_addSmsFragment,  AddSMSFragment.args(phone))
    }

    override fun goToAddName() {
        navController?.navigate(R.id.action_addSmsFragment_to_addNameFragment)
    }

    override fun goToHomeFragmentFromLogo() {
        navController?.navigate(R.id.action_logoFragment_to_homeFragment)
    }

    override fun goToGetOffersFromDriversFragment() {
        navController?.navigate(R.id.action_homeFragment_to_getOffersFromDriversFragment)
    }

}