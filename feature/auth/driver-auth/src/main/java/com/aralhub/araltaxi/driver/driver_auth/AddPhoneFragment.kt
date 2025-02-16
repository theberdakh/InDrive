package com.aralhub.araltaxi.driver.driver_auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.driver.driver_auth.navigation.FeatureDriverAuthNavigation
import com.aralhub.indrive.driver.auth.R
import com.aralhub.indrive.driver.auth.databinding.FragmentAddPhoneBinding
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.PhoneNumberFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPhoneFragment : Fragment(R.layout.fragment_add_phone) {
    private val binding by viewBinding(FragmentAddPhoneBinding::bind)
    @Inject
    lateinit var navigator: FeatureDriverAuthNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableEdgeToEdge(statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.WHITE,
            darkScrim = resources.getColor(com.aralhub.ui.R.color.color_interactive_control),
            detectDarkMode = { resources ->
                false
            }
        ))

        PhoneNumberFormatter(binding.etPhone) { isFinished ->
            binding.btnStart.isEnabled = isFinished
            if (isFinished) {
                KeyboardUtils.hideKeyboardFragment(requireContext(), binding.etPhone)
            }
        }

        binding.btnStart.setOnClickListener {
            navigator.goToAddSMSCode(binding.etPhone.text.toString())
        }
    }

}