package com.aralhub.araltaxi.profile.driver.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.driver.R
import com.aralhub.araltaxi.profile.driver.databinding.FragmentProfileBinding
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.getDriverProfile()
        viewModel.profileUiState.onEach {
            when(it){
                is ProfileUiState.Error -> Log.i("RequestFragment", "profileUiState: error ${it.message}")
                ProfileUiState.Loading -> Log.i("RequestFragment", "profileUiState: loading")
                is ProfileUiState.Success -> displayProfile(it.driverProfile)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun displayProfile(driverProfile: DriverProfile) {
        binding.tvName.text = driverProfile.fullName
        binding.tvPhone.text = driverProfile.phoneNumber
    }

    private fun initListeners() {
        binding.tbProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}