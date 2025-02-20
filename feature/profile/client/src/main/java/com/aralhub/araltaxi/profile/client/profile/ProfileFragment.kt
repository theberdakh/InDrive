package com.aralhub.araltaxi.profile.client.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.client.R
import com.aralhub.araltaxi.profile.client.databinding.FragmentProfileBinding
import com.aralhub.indrive.core.data.model.client.ClientProfile
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.getProfile()
        viewModel.profileUiState.onEach {
            when(it){
                is ProfileUiState.Error -> Log.i("ProfileFragment", "error: $it" )
                ProfileUiState.Loading -> Log.i("ProfileFragment", "loading: $it" )
                is ProfileUiState.Success ->  displayProfile(it.profile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayProfile(profile: ClientProfile) {
        binding.tvName.text = profile.fullName
        binding.tvPhone.text = profile.phone
    }

    private fun initListeners() {
        binding.tbProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}