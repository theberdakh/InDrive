package com.aralhub.araltaxi.request.navigation

import com.aralhub.ui.model.args.SelectedLocations


interface FeatureRequestNavigation {
    fun goToSelectFromLocationFromRequestFragment()
    fun goToSelectToLocationFromRequestFragment()
    fun goToCreateOrderFromRequestFragment(selectedLocations: SelectedLocations)
    fun goToGetOffersFromSendOrderFragment()
    fun goToProfileFromRequestFragment()
    fun goToSupportFromRequestFragment()
    fun goToSavedPlacesFromRequestFragment()
    fun goToHistoryFromRequestFragment()
    fun goToLogoFromRequestFragment()
}