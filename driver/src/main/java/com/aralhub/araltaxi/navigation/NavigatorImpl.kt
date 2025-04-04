package com.aralhub.araltaxi.navigation

import android.os.Bundle
import androidx.navigation.NavController
import com.aralhub.araltaxi.driver.R
import com.aralhub.araltaxi.driver.driver_auth.addsms.AddSMSFragment
import com.aralhub.araltaxi.driver.driver_auth.navigation.FeatureDriverAuthNavigation
import com.aralhub.araltaxi.driver.orders.navigation.FeatureOrdersNavigation
import com.aralhub.araltaxi.driver.orders.orders.OrdersFragment
import com.aralhub.araltaxi.driver.orders.orders.OrdersFragmentDirections
import com.aralhub.overview.OverviewFragmentDirections
import com.aralhub.overview.navigation.FeatureOverviewNavigation
import com.aralhub.ui.model.OrderItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator, FeatureDriverAuthNavigation,
    FeatureOverviewNavigation, FeatureOrdersNavigation {

    private var navController: NavController? = null

    override fun goToAddPhoneNumber() {
        navController?.navigate(R.id.action_logoFragment_to_addPhoneFragment)
    }

    override fun goToOverviewFromAddSMS() {
        navController?.navigate(R.id.action_addSmsFragment_to_overviewFragment)
    }

    override fun goToAddSMSCode(phone: String) {
        navController?.navigate(
            R.id.action_addPhoneFragment_to_addSmsFragment,
            AddSMSFragment.args(phone)
        )
    }

    override fun goToOverviewFromLogo() {
        navController?.navigate(R.id.action_logoFragment_to_overviewFragment)
    }

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        this.navController = null
    }

    override fun goToAcceptOrders(argument: Bundle?) {
        navController?.navigate(R.id.action_overviewFragment_to_ordersFragment, argument)
    }

    override fun goToProfileFromOverview() {
        navController?.navigate(R.id.action_overviewFragment_to_profileFragment)
    }

    override fun goToSupportFromOverview() {
        navController?.navigate(R.id.action_overviewFragment_to_supportFragment)
    }

    override fun goToHistoryFromOverview() {
        navController?.navigate(R.id.action_overviewFragment_to_historyFragment)
    }

    override fun goToRevenueFromOverview() {
        navController?.navigate(R.id.action_overviewFragment_to_revenueFragment)
    }

    override fun goToLogoFromOverview() {
        navController?.navigate(R.id.action_overviewFragment_to_logoFragment)
    }

    override fun goToMapFromOrders(order: OrderItem) {
        val bundle = Bundle()
        bundle.putParcelable("OrderDetail", order)
        val direction = R.id.action_ordersFragment_to_showOrderRouteFragment
        navController?.navigate(
            direction,
            bundle
        )
    }

    override fun goToProfileFromOrders() {
        navController?.navigate(R.id.action_ordersFragment_to_profileFragment)
    }

    override fun goToSupportFromOrders() {
        navController?.navigate(R.id.action_ordersFragment_to_supportFragment)
    }

    override fun goToHistoryFromOrders() {
        navController?.navigate(R.id.action_ordersFragment_to_historyFragment)
    }

    override fun goToRevenueFromOrders() {
        navController?.navigate(R.id.action_ordersFragment_to_revenueFragment)
    }

    override fun goToLogoFromOrders() {
        navController?.navigate(R.id.action_ordersFragment_to_logoFragment)
    }
}