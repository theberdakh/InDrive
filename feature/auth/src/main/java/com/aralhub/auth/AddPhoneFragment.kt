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
import com.aralhub.ui.utils.PhoneNumberFormatter
import com.aralhub.ui.utils.viewBinding

class AddPhoneFragment : Fragment(R.layout.fragment_add_phone) {
    private val binding by viewBinding(FragmentAddPhoneBinding::bind)
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

        }
        binding.btnStart.setOnClickListener {
            hideKeyboard(it)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }
}