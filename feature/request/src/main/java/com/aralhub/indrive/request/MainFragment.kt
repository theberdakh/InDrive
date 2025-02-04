package com.aralhub.indrive.request

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.indrive.request.databinding.FragmentMainBinding
import com.aralhub.indrive.request.navigation.BottomSheetNavigator
import com.aralhub.network.utils.NetworkMonitor
import com.aralhub.ui.components.crouton.Crouton
import com.aralhub.ui.utils.CroutonInDriveStyle
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    @Inject
    lateinit var bottomSheetNavigator: BottomSheetNavigator
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BottomSheet", "OnViewCreated")
        lifecycleScope.launch { monitorNetworkConnection() }
        setUpDrawerLayout()
        setUpBottomSheet()
        initMapController()
        initBottomNavController()
    }

    private fun initMapController() {
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutBottomSheet.isVisible = true
    }

    private fun setUpDrawerLayout() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                bottomSheetBehavior?.apply {
                    isHideable = true
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                bottomSheetBehavior?.apply {
                    isHideable = true
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            override fun onDrawerClosed(drawerView: View) {
                bottomSheetBehavior?.apply {
                    isHideable = false
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    private fun initBottomNavController() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.bottom_sheet_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        navController.let {
            bottomSheetNavigator.bind(navController)
        }
    }


    private suspend fun monitorNetworkConnection() {
        var isDisconnected = false
        networkMonitor.isOnline.collect {
            if (!it) {
                isDisconnected = true
                Crouton.makeText(
                    requireActivity(),
                    com.aralhub.ui.R.string.error_network_connection,
                    CroutonInDriveStyle.errorStyle
                ).show()
            } else if (isDisconnected) {
                Crouton.makeText(
                    requireActivity(),
                    com.aralhub.ui.R.string.success_network_connection,
                    CroutonInDriveStyle.successStyle
                ).show()
            }
        }
    }

}