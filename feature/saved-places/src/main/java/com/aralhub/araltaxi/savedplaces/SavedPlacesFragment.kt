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
import com.aralhub.araltaxi.savedplaces.adapter.AddressCategory
import com.aralhub.araltaxi.savedplaces.adapter.AddressItem
import com.aralhub.araltaxi.savedplaces.adapter.AddressItemAdapter
import com.aralhub.araltaxi.savedplaces.navigation.FeatureSavedPlaceNavigation
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.ui.utils.ViewEx.hide
import com.aralhub.ui.utils.ViewEx.show
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SavedPlacesFragment : Fragment(R.layout.fragment_saved_places) {
    private val binding by viewBinding(FragmentSavedPlacesBinding::bind)
    private val viewModel by viewModels<SavedPlacesViewModel>()
    @Inject lateinit var errorHandler: ErrorHandler
    @Inject lateinit var navigation: FeatureSavedPlaceNavigation
    private val adapter = AddressItemAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        binding.rvSavedPlaces.adapter = adapter
    }

    private fun initObservers() {
        viewModel.getAllSavedAddresses(38)
        viewModel.savedPlacesUiState.onEach {
            when (it) {
                is SavedPlacesUiState.Error -> errorHandler.showToast(it.message)
                SavedPlacesUiState.Loading -> {}
                is SavedPlacesUiState.Success -> {
                    if (it.data.isNotEmpty()){
                        binding.tvNoSavedPlaces.hide()
                        adapter.submitList(it.data.map { address ->
                            AddressItem(
                                id = address.id,
                                name = address.name,
                                address = address.address,
                                category = AddressCategory.OTHER,
                                latitude = address.latitude,
                                longitude = address.longitude
                            )
                        })
                    } else {
                        binding.tvNoSavedPlaces.show()
                    }

                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.createAddressUiState.onEach {
            when (it) {
                is CreateAddressUiState.Error -> errorHandler.showToast(it.message)
                CreateAddressUiState.Loading -> {}
                is CreateAddressUiState.Success -> {
                    errorHandler.showToast("Address created successfully")
                    viewModel.getAllSavedAddresses(38)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initListeners() {
        adapter.setOnItemClickListener {
            navigation.navigateToEditSavedPlace(it.id)
        }
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