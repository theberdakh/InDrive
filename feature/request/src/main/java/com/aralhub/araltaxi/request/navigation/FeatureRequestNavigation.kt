package com.aralhub.araltaxi.request.navigation

import com.aralhub.araltaxi.request.navigation.models.SelectedLocations

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