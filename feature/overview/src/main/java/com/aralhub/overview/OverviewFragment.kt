package com.aralhub.overview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.overview.R
import com.aralhub.araltaxi.overview.databinding.FragmentOverviewBinding
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.overview.navigation.FeatureOverviewNavigation
import com.aralhub.overview.sheet.LogoutModalBottomSheet
import com.aralhub.overview.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private val binding by viewBinding(FragmentOverviewBinding::bind)
    @Inject lateinit var navigation: FeatureOverviewNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
    }

    private fun initObservers() {
      /*  viewModel.getProfile()
        viewModel.profileUiState.onEach {
            when(it){
                is ProfileUiState.Error -> Log.i("ProfileUi", "Error ${it.message}")
                ProfileUiState.Loading -> Log.i("ProfileUi", "Loading")
                is ProfileUiState.Success -> {
                    Log.i("ProfileUi", "Success ${it.profile}")
                    displayProfile(it.profile)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)*/
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

    private fun displayProfile(profile: DriverProfile) {
     /*   binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phone*/

/*
        Glide.with(this)
            .load("https://araltaxi.aralhub.uz/${profile.profilePhoto}")
            .apply(RequestOptions.circleCropTransform())
            .signature(ObjectKey(System.currentTimeMillis()))
            .into(binding.navigationView.getHeaderView(0).findViewById(R.id.iv_avatar))*/
    }

    private fun setUpDrawerLayout() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        binding.drawerLayout.addDrawerListener(BottomSheetBehaviorDrawerListener(bottomSheetBehavior))
    }
}