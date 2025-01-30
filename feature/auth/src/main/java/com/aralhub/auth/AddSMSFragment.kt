package com.aralhub.auth

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aralhub.auth.databinding.FragmentAddSmsBinding
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddSMSFragment : Fragment(R.layout.fragment_add_sms) {
    private val binding by viewBinding(FragmentAddSmsBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = requireArguments().getString(ARG_PHONE) ?: ""
        val fullText = getString(com.aralhub.ui.R.string.label_confirm_description, phoneNumber)
        binding.tvDescription.text = setPhoneNumberSpan(fullText, phoneNumber)

        binding.etPhone.addTextChangedListener {
            binding.btnStart.isEnabled = it.toString().length == 4
            if(binding.btnStart.isEnabled){
                KeyboardUtils.hideKeyboardFragment(requireContext(), binding.etPhone)
            }
        }

        binding.btnStart.setOnClickListener {
           lifecycleScope.launch {
                delay(3000)
                binding.btnStart.stopProgress()
            }
        }

    }

    private fun setPhoneNumberSpan(fullText: String, phoneNumber: String): SpannableString {
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(phoneNumber)
        val endIndex = startIndex + phoneNumber.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(com.aralhub.ui.R.color.color_interactive_control)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    companion object {
        private const val ARG_PHONE = "phone"

        fun args(phone: String): Bundle {
            return Bundle().apply {
                putString(ARG_PHONE, phone)
            }
        }
    }
}