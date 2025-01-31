package com.aralhub.auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.aralhub.auth.databinding.FragmentLogoBinding
import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogoFragment : Fragment(R.layout.fragment_logo) {
    private val binding by viewBinding(FragmentLogoBinding::bind)

    @Inject
    lateinit var navigator: FeatureAuthNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.BLACK))

        binding.viewNextButton.setOnClickListener {
            navigator.goToRequestTaxiFromLogo()
        }
    }
}