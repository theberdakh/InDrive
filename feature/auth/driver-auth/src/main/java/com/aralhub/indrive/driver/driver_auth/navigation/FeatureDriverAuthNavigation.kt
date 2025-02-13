package com.aralhub.indrive.driver.driver_auth.navigation

interface FeatureDriverAuthNavigation {
    fun goToAddPhoneNumber()
    fun goToOverviewFromAddName()
    fun goToAddSMSCode(phone: String)
    fun goToAddName()
    fun goToOverviewFromLogo()
}