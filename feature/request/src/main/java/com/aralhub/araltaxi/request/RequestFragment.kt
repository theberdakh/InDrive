package com.aralhub.araltaxi.request

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.FragmentRequestBinding
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.SheetNavigator
import com.aralhub.araltaxi.request.sheet.modal.LogoutModalBottomSheet
import com.aralhub.araltaxi.request.utils.BottomSheetBehaviorDrawerListener
import com.aralhub.araltaxi.request.utils.MapKitInitializer
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestFragment : Fragment(R.layout.fragment_request) {
    private val binding by viewBinding(FragmentRequestBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private val requiredPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    @Inject lateinit var sheetNavigator: SheetNavigator
    @Inject lateinit var navigation: FeatureRequestNavigation
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { permission ->
                Log.i(
                    "RequestTaxiFragment",
                    "Permission: ${permission.key} is granted: ${permission.value}"
                )
            }
        }
    private val viewModel by viewModels<RequestViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitInitializer.init("eb911707-c4c6-4608-9917-f22012813a34", requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchPermissions()
        initViews()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getProfile()
        viewModel.profileUiState.onEach {
            when(it){
                is ProfileUiState.Error -> Log.i("RequestFragment", "profileUiState: error ${it.message}")
                ProfileUiState.Loading -> Log.i("RequestFragment", "profileUiState: loading")
                is ProfileUiState.Success -> displayProfile(it.profile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.logOutUiState.onEach {
            when(it){
                is LogOutUiState.Error -> Log.i("RequestFragment", "logOutUiState: error ${it.message}")
                LogOutUiState.Loading -> Log.i("RequestFragment", "logOutUiState: loading")
                LogOutUiState.Success -> navigation.goToLogoFromRequestFragment()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayProfile(profile: ClientProfile) {
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            profile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            profile.phone

        Log.i("My Url", "url: ${profile.profilePhoto}")

        Glide.with(this)
            .load("https://araltaxi.aralhub.uz/${profile.profilePhoto}")
            .apply(RequestOptions.circleCropTransform())
            .signature(ObjectKey(System.currentTimeMillis()))
            .into(binding.navigationView.getHeaderView(0).findViewById(R.id.iv_avatar))
    }

    private fun launchPermissions() {
        locationPermissionLauncher.launch(requiredPermissions)
    }

    private fun initViews() {
        setUpMapView()
        setUpBottomSheet()
        initBottomNavController()
    }

    private fun initListeners() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navigation.goToProfileFromRequestFragment()
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromRequestFragment()
                    true
                }
                R.id.action_log_out -> {
                    val logOutModalBottomSheet = LogoutModalBottomSheet()
                    logOutModalBottomSheet.show(childFragmentManager, LogoutModalBottomSheet.TAG)
                    logOutModalBottomSheet.setOnLogoutListener {
                        logOutModalBottomSheet.dismissAllowingStateLoss()
                        viewModel.logOut()
                    }
                    true
                }
                R.id.action_my_addresses -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSavedPlacesFromRequestFragment()
                    true
                }
                R.id.action_trip_history -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToHistoryFromRequestFragment()
                    true
                }
                else -> false
             }
        }
        if (bottomSheetBehavior != null) {
            binding.drawerLayout.addDrawerListener(
                BottomSheetBehaviorDrawerListener(
                    bottomSheetBehavior!!
                )
            )
        }
    }

    private fun setUpMapView() {
        binding.mapView.onStart()
        binding.mapView.mapWindow.map.move(
            CameraPosition(
                Point(42.4619, 59.6166),
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 30.0f
            )
        )
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutBottomSheet.isVisible = true
    }

    private fun initBottomNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.bottom_sheet_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let { sheetNavigator.bind(navController) }
    }

    override fun onDestroy() {
        sheetNavigator.unbind()
        super.onDestroy()
    }
}