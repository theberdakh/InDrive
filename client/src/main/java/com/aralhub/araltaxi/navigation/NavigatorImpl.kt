package com.aralhub.araltaxi.navigation

import androidx.navigation.NavController
import com.aralhub.araltaxi.client.R
import com.aralhub.araltaxi.profile.client.navigation.FeatureProfileNavigation
import com.aralhub.client.clientauth.addsms.AddSMSFragment
import com.aralhub.client.clientauth.navigation.FeatureClientAuthNavigation
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.savedplaces.editsavedplace.EditSavedPlaceFragment
import com.aralhub.araltaxi.savedplaces.navigation.FeatureSavedPlaceNavigation
import com.aralhub.offers.navigation.FeatureOffersNavigation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(): Navigator, FeatureClientAuthNavigation, FeatureRequestNavigation, FeatureOffersNavigation, FeatureProfileNavigation, FeatureSavedPlaceNavigation{

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

    override fun goToSelectLocationFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_selectLocationFragment)
    }

    override fun goToGetOffersFromSendOrderFragment() {
        navController?.navigate(R.id.action_requestFragment_to_offersFragment)
    }

    override fun goToProfileFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_profileFragment)
    }

    override fun goToSupportFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_supportFragment)
    }

    override fun goToSavedPlacesFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_savedPlacesFragment)
    }

    override fun goToHistoryFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_historyFragment)
    }

    override fun goToLogoFromRequestFragment() {
        navController?.navigate(R.id.action_requestFragment_to_logoFragment)
    }

    override fun goToRideFragment() {
        navController?.navigate(R.id.action_offersFragment_to_rideFragment)
    }

    override fun goBackToRequestFragmentFromOffersFragment() {
        navController?.navigate(R.id.action_offersFragment_to_requestFragment)
    }

    override fun goToLogoFragmentFromProfileFragment() {
        navController?.navigate(R.id.action_profileFragment_to_logoFragment)
    }

    override fun navigateToEditSavedPlace(savedPlaceId: Int) {
        navController?.navigate(R.id.action_savedPlacesFragment_to_editSavedPlaceFragment, EditSavedPlaceFragment.args(savedPlaceId))
    }
}