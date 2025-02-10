package com.aralhub.indrivedriver.navigation

import androidx.navigation.NavController
import com.aralhub.auth.AddSMSFragment
import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.indrivedriver.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor(): Navigator, FeatureAuthNavigation{

    private var navController: NavController? = null

    override fun goToAddPhoneNumber() {
        navController?.navigate(R.id.action_logoFragment_to_addPhoneFragment)
    }

    override fun goToRequestFromAddName() {
        // driver does not have request
    }
    override fun goToOrdersFromAddName() {
        navController?.navigate(R.id.action_addNameFragment_to_ordersFragment)
    }

    override fun goToAddSMSCode(phone: String) {
        navController?.navigate(R.id.action_addPhoneFragment_to_addSmsFragment, AddSMSFragment.args(phone))
    }

    override fun goToAddName() {
        navController?.navigate(R.id.action_addSmsFragment_to_addNameFragment)
    }

    override fun goToRequestFromLogo(){
        // driver does not have request
    }
    override fun goToOrdersFromLogo() {
        navController?.navigate(R.id.action_logoFragment_to_ordersFragment)
    }

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }
}