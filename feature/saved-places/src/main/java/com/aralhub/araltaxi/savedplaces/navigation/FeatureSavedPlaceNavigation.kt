package com.aralhub.araltaxi.savedplaces.navigation

interface FeatureSavedPlaceNavigation {
    fun navigateToEditSavedPlace(savedPlaceId: Int)
    fun navigateToSelectLocationFromSavedPlaces()
    fun navigateToSaveAddressFromSavedPlaces(name: String, address: String, latitude: Double, longitude: Double)
}