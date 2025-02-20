package com.aralhub.client.clientauth.navigation

interface FeatureClientAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToRequestFromAddName()
    fun goToAddSMSCode(phone: String)
    fun goToAddName()
    fun goToRequestFromLogo()
}