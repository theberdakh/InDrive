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
import com.aralhub.indrive.core.data.model.driver.DriverProfileWithVehicle
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
        viewModel.getDriverCard()
        viewModel.cardUiState.onEach {
            when(it){
                is CardUiState.Error -> Log.i("RequestFragment", "cardUiState: error ${it.message}")
                CardUiState.Loading -> Log.i("RequestFragment", "cardUiState: loading")
                is CardUiState.Success -> displayCard(it.card, it.cardHolder)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getDriverProfileWithVehicle()
        viewModel.profileWithVehicleUiState.onEach {
            when(it){
                is ProfileWithVehicleUiState.Error -> Log.i("RequestFragment", "profileWithVehicleUiState: error ${it.message}")
                ProfileWithVehicleUiState.Loading -> Log.i("RequestFragment", "profileWithVehicleUiState: loading")
                is ProfileWithVehicleUiState.Success -> displayProfileWithVehicle(it.driverProfileWithVehicle)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun displayProfileWithVehicle(driverProfileWithVehicle: DriverProfileWithVehicle) {
        binding.tvCar.text = driverProfileWithVehicle.vehicleType
        binding.tvCarNumber.text = driverProfileWithVehicle.plateNumber
        binding.tvCarColor.text = driverProfileWithVehicle.color
    }

    private fun displayCard(cardNumber: String, nameOnCard: String) {
        binding.tvCard.text = cardNumber
        binding.tvCardLabel.text = nameOnCard
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