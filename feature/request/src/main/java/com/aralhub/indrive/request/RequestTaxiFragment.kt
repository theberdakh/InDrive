package com.aralhub.indrive.request

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.aralhub.indrive.request.databinding.FragmentRequestTaxiBinding
import com.aralhub.ui.components.crouton.Crouton
import com.aralhub.ui.utils.CroutonInDriveStyle
import com.aralhub.ui.utils.viewBinding

class RequestTaxiFragment : Fragment(R.layout.fragment_request_taxi) {
    private val binding by viewBinding(FragmentRequestTaxiBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.WHITE,
            darkScrim = resources.getColor(com.aralhub.ui.R.color.color_interactive_control),
            detectDarkMode = { resources ->
                false
            }
        ))

    }
}