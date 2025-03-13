package com.aralhub.overview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.overview.R
import com.aralhub.araltaxi.overview.databinding.FragmentOverviewBinding
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.overview.navigation.FeatureOverviewNavigation
import com.aralhub.overview.sheet.LocationServiceOffModalBottomSheet
import com.aralhub.overview.sheet.LogoutModalBottomSheet
import com.aralhub.overview.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.overview.utils.isGPSEnabled
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

    private var pLauncher: ActivityResultLauncher<Array<String>>? = null

    private val locationServiceOffModalBottomSheet = LocationServiceOffModalBottomSheet()

    @Inject
    lateinit var navigation: FeatureOverviewNavigation
    private val viewModel by viewModels<OverviewViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
        initListeners()
        registerPermissionListener()
        checkLocationPermission()
        //viewModel.getProfile()
        viewModel.getBalance()
    }

    private fun initObservers() {
//        viewModel.getProfile()
        viewModel.profileUiState.onEach {
            when (it) {
                is ProfileUiState.Error -> Log.i(
                    "RequestFragment",
                    "profileUiState: error ${it.message}"
                )

                ProfileUiState.Loading -> Log.i("RequestFragment", "profileUiState: loading")
                is ProfileUiState.Success -> displayProfile(it.profile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.balanceUiState.onEach {
            when (it) {
                is DriverBalanceUiState.Error -> Log.i(
                    "RequestFragment",
                    "balanceUiState: error ${it.message}"
                )

                DriverBalanceUiState.Loading -> Log.i("RequestFragment", "balanceUiState: loading")
                is DriverBalanceUiState.Success -> {
                    binding.tvBalance.text = it.balance.toString()
                    binding.tvDailyIncome.text = it.dayBalance.toString()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.logoutUiState.onEach {
            when (it) {
                is LogoutUiState.Error -> Log.i(
                    "RequestFragment",
                    "logoutUiState: error ${it.message}"
                )

                LogoutUiState.Loading -> Log.i("RequestFragment", "logoutUiState: loading")
                LogoutUiState.Success -> navigation.goToLogoFromOverview()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initListeners() {
        binding.btnAcceptOrders.setOnClickListener {
            // check for location, if not enabled show: LocationServiceOffModalBottomSheet().show(childFragmentManager, LocationServiceOffModalBottomSheet.TAG)
            navigation.goToAcceptOrders()
        }
        binding.tbOverview.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(
                GravityCompat.START
            )
        }
        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navigation.goToProfileFromOverview()
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromOverview()
                    true
                }

                R.id.action_log_out -> {
                    val logoutModalBottomSheet = LogoutModalBottomSheet()
                    logoutModalBottomSheet.show(childFragmentManager, LogoutModalBottomSheet.TAG)
                    logoutModalBottomSheet.setOnLogoutListener {
                        logoutModalBottomSheet.dismissAllowingStateLoss()
                        viewModel.logout()
                    }
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
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            profile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phoneNumber
        Glide.with(requireContext())
            .load("https://araltaxi.aralhub.uz/${profile.photoUrl}")
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .signature(ObjectKey(System.currentTimeMillis()))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_avatar))

    }

    private fun setUpDrawerLayout() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        binding.drawerLayout.addDrawerListener(BottomSheetBehaviorDrawerListener(bottomSheetBehavior))
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                checkGpsEnabled()
            }

            else -> {
                pLauncher?.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGPSEnabled(requireContext()) && locationServiceOffModalBottomSheet.isAdded)
            locationServiceOffModalBottomSheet.dismiss()
    }

    private fun checkGpsEnabled() {
        if (!isGPSEnabled(requireContext()))
            locationServiceOffModalBottomSheet.show(
                childFragmentManager,
                LocationServiceOffModalBottomSheet.TAG
            )
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                Log.d("OverviewFragment", "go to accept orders")
                checkGpsEnabled()
            } else {
                Log.d("OverviewFragment", "Permission denied")
            }
        }
    }
}