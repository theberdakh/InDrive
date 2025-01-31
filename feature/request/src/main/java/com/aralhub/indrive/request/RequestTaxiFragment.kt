package com.aralhub.indrive.request

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aralhub.indrive.request.databinding.FragmentRequestTaxiBinding
import com.aralhub.network.utils.NetworkMonitor
import com.aralhub.ui.components.crouton.Crouton
import com.aralhub.ui.utils.CroutonInDriveStyle
import com.aralhub.ui.utils.viewBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RequestTaxiFragment : Fragment(R.layout.fragment_request_taxi) {
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    private val binding by viewBinding(FragmentRequestTaxiBinding::bind)
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { permission ->
            Log.i(
                "RequestTaxiFragment",
                "Permission: ${permission.key} is granted: ${permission.value}"
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MapKitFactory.setLocale("ru_RU")
        MapKitFactory.setApiKey("eb911707-c4c6-4608-9917-f22012813a34")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            monitorNetworkConnection()
        }
        setLightStatusBar()
        locationPermissionLauncher.launch(requiredPermissions)
        setUpMapView()


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
            } else if(isDisconnected){
                Crouton.makeText(
                    requireActivity(),
                    com.aralhub.ui.R.string.success_network_connection,
                    CroutonInDriveStyle.successStyle
                ).show()
            }
        }
    }

    private fun setLightStatusBar() {
        requireActivity().enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.WHITE,
            darkScrim = resources.getColor(com.aralhub.ui.R.color.color_interactive_control),
            detectDarkMode = { resources ->
                false
            }
        ))
    }
}