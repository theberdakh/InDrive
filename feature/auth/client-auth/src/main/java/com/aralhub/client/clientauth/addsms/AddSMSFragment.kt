package com.aralhub.client.clientauth.addsms

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.auth.R
import com.aralhub.araltaxi.client.auth.databinding.FragmentAddSmsBinding
import com.aralhub.client.clientauth.addphone.AddPhoneViewModel
import com.aralhub.client.clientauth.navigation.FeatureClientAuthNavigation
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.ViewEx.hide
import com.aralhub.ui.utils.ViewEx.show
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddSMSFragment : Fragment(R.layout.fragment_add_sms) {
    private val binding by viewBinding(FragmentAddSmsBinding::bind)
    private var _phone: String = ""
    private val boldTextHex = "#001934"
    private val viewModel by viewModels<AddSMSViewModel>()
    @Inject lateinit var navigator: FeatureClientAuthNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initViews()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.addSMSUiState.onEach { state ->
            Log.i("AddSMSFragment", "initObservers: $state")
            when(state){
                is AddSMSUiState.Error -> {
                    displayLoading(show = false)
                    displayError(errorMessage = state.message)
                }
                AddSMSUiState.Loading -> displayLoading(show = true)
                AddSMSUiState.Success -> {
                    displayLoading(show = false)
                    navigateToAddName()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun navigateToAddName() {
        navigator.goToAddName()
    }

    private fun displayError(errorMessage: String) {
        binding.tvError.show()
        binding.tvError.text = errorMessage
    }

    private fun displayLoading(show: Boolean) {
        if (show) {
            binding.btnStart.startProgress()
        } else {
            binding.btnStart.stopProgress()
        }
    }

    private fun initListeners() {
        binding.etPhone.addTextChangedListener {
            binding.tvError.hide()
            binding.btnStart.isEnabled = it.toString().length == 5
            if (binding.btnStart.isEnabled) { KeyboardUtils.hideKeyboardFragment(requireContext(), binding.etPhone) }
        }
        binding.btnStart.setOnClickListener {
            val code = binding.etPhone.text.toString()
            viewModel.verifyPhone(_phone, code)
        }
    }

    private fun initArgs() { _phone = requireArguments().getString(ARG_PHONE) ?: "" }

    private fun initViews() {
        val fullText = getString(com.aralhub.ui.R.string.label_confirm_description, _phone)
        binding.tvDescription.text = StringUtils.getBoldSpanString(fullText = fullText, boldText = _phone, boldTextColorHex = boldTextHex)
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