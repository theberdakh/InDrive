package com.aralhub.auth.navigation

interface FeatureAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToRequestFromAddName()
    fun goToOrdersFromAddName()
    fun goToAddSMSCode(phone: String)
    fun goToAddName()
    fun goToRequestFromLogo()
    fun goToOrdersFromLogo()
}