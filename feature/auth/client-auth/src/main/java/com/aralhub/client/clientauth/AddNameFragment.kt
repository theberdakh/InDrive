package com.aralhub.client.clientauth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.auth.R
import com.aralhub.araltaxi.client.auth.databinding.FragmentAddNameBinding
import com.aralhub.client.client_auth.viewmodel.AuthViewModel
import com.aralhub.client.clientauth.navigation.FeatureClientAuthNavigation
import com.aralhub.indrive.core.data.model.client.ClientVerifyRequest
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddNameFragment : Fragment(R.layout.fragment_add_name) {
    private val binding by viewBinding(FragmentAddNameBinding::bind)
    @Inject
    lateinit var navigator: FeatureClientAuthNavigation

    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.addTextChangedListener {
            binding.btnSave.isEnabled = it.toString().isNotEmpty()
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                sendRequest("")
            }
        }

    }

    private fun sendRequest(name: String) {
        viewModel.addName(
            "Atabek"
        )
        viewModel.authState.onEach { state ->
            when (state) {
                is AuthViewModel.LoginUiState.Error -> Log.d("LoginState", state.message)
                AuthViewModel.LoginUiState.Loading -> Log.d("LoginState", "loading")
                AuthViewModel.LoginUiState.Success -> navigator.goToRequestFromAddName()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}