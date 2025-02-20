package com.aralhub.overview

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.overview.R
import com.aralhub.araltaxi.overview.databinding.FragmentOverviewBinding
import com.aralhub.overview.navigation.FeatureOverviewNavigation
import com.aralhub.overview.sheet.LogoutModalBottomSheet
import com.aralhub.overview.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private val binding by viewBinding(FragmentOverviewBinding::bind)

    @Inject
    lateinit var navigation: FeatureOverviewNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.btnAcceptOrders.setOnClickListener {
            // check for location, if not enabled show: LocationServiceOffModalBottomSheet().show(childFragmentManager, LocationServiceOffModalBottomSheet.TAG)
            navigation.goToAcceptOrders()
        }
        binding.tbOverview.setNavigationOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }
        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navigation.goToProfileFromOverview()
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromOverview()
                    true
                }
                R.id.action_log_out -> {
                    LogoutModalBottomSheet.show(childFragmentManager)
                    true
                }
                R.id.action_my_revenue -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToRevenueFromOverview()
                    true
                }
                R.id.action_order_history -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToHistoryFromOverview()
                    true
                }
                else -> false
            }
        }
    }

    private fun initViews() {
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        binding.drawerLayout.addDrawerListener(BottomSheetBehaviorDrawerListener(bottomSheetBehavior))
    }
}