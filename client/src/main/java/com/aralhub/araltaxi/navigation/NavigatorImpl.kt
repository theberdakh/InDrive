package com.aralhub.araltaxi.navigation

import androidx.navigation.NavController
import com.aralhub.araltaxi.client.R
import com.aralhub.client.clientauth.addsms.AddSMSFragment
import com.aralhub.client.clientauth.navigation.FeatureClientAuthNavigation
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.offers.navigation.FeatureOffersNavigation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(): Navigator, FeatureClientAuthNavigation, FeatureRequestNavigation, FeatureOffersNavigation{

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

    override fun goToRequestFromAddName() {
        navController?.navigate(R.id.action_addNameFragment_to_requestFragment)
    }

    override fun goToAddSMSCode(phone: String) {
        navController?.navigate(R.id.action_addPhoneFragment_to_addSmsFragment,  AddSMSFragment.args(phone))
    }

    override fun goToAddName() {
        navController?.navigate(R.id.action_addSmsFragment_to_addNameFragment)
    }

    override fun goToRequestFromLogo() {
        navController?.navigate(R.id.action_logoFragment_to_requestFragment)
    }

    override fun goToGetOffersFromSendOrderFragment() {
        navController?.navigate(R.id.action_homeFragment_to_offersFragment)
    }

    override fun goToProfileFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_profileFragment)
    }

    override fun goToSupportFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_supportFragment)
    }

    override fun goToRideFragment() {
        navController?.navigate(R.id.action_offersFragment_to_rideFragment)
    }
}