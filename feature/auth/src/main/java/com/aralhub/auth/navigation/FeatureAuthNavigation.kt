package com.aralhub.auth.navigation

interface FeatureAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToRequestTaxiFromLogo()
    fun goToRequestTaxiFromAddName()
    fun goToAddSMSCode(phone: String)
    fun goToAddName()
}