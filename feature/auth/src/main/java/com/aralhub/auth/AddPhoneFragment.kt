package com.aralhub.auth

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.aralhub.auth.databinding.FragmentAddPhoneBinding
import com.aralhub.auth.navigation.FeatureAuthNavigation
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.PhoneNumberFormatter
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPhoneFragment : Fragment(R.layout.fragment_add_phone) {
    private val binding by viewBinding(FragmentAddPhoneBinding::bind)

    @Inject
    lateinit var featureAuthNavigation: FeatureAuthNavigation
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
            featureAuthNavigation.goToAddSMSCode(binding.etPhone.text.toString())
        }
    }

}