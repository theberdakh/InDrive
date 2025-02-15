package com.aralhub.client.client_auth

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.auth.R
import com.aralhub.araltaxi.client.auth.databinding.FragmentAddPhoneBinding
import com.aralhub.client.client_auth.model.AuthRequestUI
import com.aralhub.client.client_auth.navigation.FeatureClientAuthNavigation
import com.aralhub.client.client_auth.viewmodel.AuthViewModel
import com.aralhub.ui.utils.KeyboardUtils
import com.aralhub.ui.utils.PhoneNumberFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class AddPhoneFragment : Fragment(R.layout.fragment_add_phone) {
    private val binding by viewBinding(FragmentAddPhoneBinding::bind)

    private val viewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var navigator: FeatureClientAuthNavigation
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
            sendRequest("+998912345678")
        }

        sendRequest("+998912345677")
    }

    private fun sendRequest(phone: String) {
        viewModel.auth(AuthRequestUI(phone))
        viewModel.authState.onEach { state ->
            when (state) {
                is AuthViewModel.LoginUiState.Error -> Log.d("LoginState", state.message)
                AuthViewModel.LoginUiState.Loading -> Log.d("LoginState", "loading")
                AuthViewModel.LoginUiState.Success -> navigator.goToAddSMSCode(binding.etPhone.text.toString())
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}