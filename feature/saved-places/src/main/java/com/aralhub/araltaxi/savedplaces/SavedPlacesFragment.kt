package com.aralhub.araltaxi.savedplaces

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.saved_places.R
import com.aralhub.araltaxi.saved_places.databinding.FragmentSavedPlacesBinding
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SavedPlacesFragment: Fragment(R.layout.fragment_saved_places) {
    private val binding by viewBinding(FragmentSavedPlacesBinding::bind)
    private val viewModel by viewModels<SavedPlacesViewModel>()
    @Inject lateinit var errorHandler: ErrorHandler
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.uiState.onEach {
            when(it){
                is HistoryUiState.Error -> errorHandler.showToast(it.message)
                HistoryUiState.Loading -> {}
                is HistoryUiState.Success -> errorHandler.showToast("Address created successfully")
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initListeners() {
        binding.tbSavedPlaces.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddPlace.setOnClickListener {
            viewModel.createAddress(
                address = CreateAddressRequest(
                    userId = 38,
                    name = "Work",
                    address = "Home Address",
                    latitude = 0.0,
                    longitude = 0.0,
                )
            )
        }
    }
}