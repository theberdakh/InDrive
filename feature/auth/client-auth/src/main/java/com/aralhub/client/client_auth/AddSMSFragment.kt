package com.aralhub.client.client_auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.auth.R
import com.aralhub.araltaxi.client.auth.databinding.FragmentAddSmsBinding
import com.aralhub.client.client_auth.viewmodel.AuthViewModel
import com.aralhub.client.clientauth.navigation.FeatureClientAuthNavigation
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class AddSMSFragment : Fragment(R.layout.fragment_add_sms) {
    private val binding by viewBinding(FragmentAddSmsBinding::bind)

    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var navigator: FeatureClientAuthNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val phoneNumber = requireArguments().getString(ARG_PHONE) ?: ""
        val fullText = getString(com.aralhub.ui.R.string.label_confirm_description, phoneNumber)
        binding.tvDescription.text = StringUtils.getBoldSpanString(fullText, phoneNumber, "#001934")
        binding.etPhone.addTextChangedListener {
            binding.btnStart.isEnabled = it.toString().length == 5
            if (binding.btnStart.isEnabled) {
                KeyboardUtils.hideKeyboardFragment(requireContext(), binding.etPhone)
            }
        }
        binding.btnStart.setOnClickListener {
            sendRequest(
                "+998913838285",
                "12345"
            )
//            lifecycleScope.launch {
//                delay(3000)
//                binding.btnStart.stopProgress()
//                navigator.goToAddName()
//            }
        }
    }

    private fun sendRequest(phone: String, code: String) {
        viewModel.verify(
            ClientVerifyRequest(
                phone,
                code
            )
        )
        viewModel.authState.onEach { state ->
            when (state) {
                is AuthViewModel.LoginUiState.Error -> Log.d("LoginState", state.message)
                AuthViewModel.LoginUiState.Loading -> Log.d("LoginState", "loading")
                AuthViewModel.LoginUiState.Success -> navigator.goToAddName()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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