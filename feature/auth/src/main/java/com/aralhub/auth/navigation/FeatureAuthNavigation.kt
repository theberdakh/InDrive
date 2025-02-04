package com.aralhub.auth.navigation

interface FeatureAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToHomeFragmentFromAddName()
    fun goToAddSMSCode(phone: String)
    fun goToAddName()
    fun goToHomeFragmentFromLogo()
}