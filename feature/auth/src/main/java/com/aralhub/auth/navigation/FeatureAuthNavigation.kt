package com.aralhub.auth.navigation

interface FeatureAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToRequestTaxi()
    fun goToAddSMSCode(phone: String)
}