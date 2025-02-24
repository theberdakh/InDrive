package com.aralhub.araltaxi.request.sheet.standard.requesttaxi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.BottomSheetRequestTaxiBinding
import com.aralhub.araltaxi.request.adapter.LocationItemAdapter
import com.aralhub.araltaxi.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.araltaxi.request.sheet.modal.addlocation.MapViewModel
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.ui.components.EndTextEditText
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class RequestTaxiBottomSheet : Fragment(R.layout.bottom_sheet_request_taxi) {
    private val binding by viewBinding(BottomSheetRequestTaxiBinding::bind)
    private val locationItemAdapter = LocationItemAdapter()
    private val viewModel by viewModels<MapViewModel>()
    private val requestTaxiBottomSheetViewModel by viewModels<RequestTaxiBottomSheetViewModel>()

    @Inject
    lateinit var navigator: FeatureRequestBottomSheetNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLocations.adapter = locationItemAdapter
        setEditTexts(binding.etFromLocation, binding.etToLocation)
        binding.etFromLocation.setOnTextChangedListener {
            viewModel.startSearch(searchText = it)
        }
        binding.etFromLocation.setEndTextClickListener {
            navigator.goToSelectLocationFromRequestTaxi()
        }
        binding.etToLocation.setEndTextClickListener {
            navigator.goToSendOrderFromRequestTaxi()
        }

        // 42.482461, 59.613237
        // 42.466078, 59.611981

        requestTaxiBottomSheetViewModel.getRecommendedPrice(
            listOf(
                GeoPoint(
                    latitude = 42.482461,
                    longitude = 59.613237,
                    name = "string"
                ), GeoPoint(
                    latitude = 42.466078,
                    longitude = 59.611981,
                    name = "string"
                )
            )
        )
        initObservers()
    }

    private fun initObservers() {
        requestTaxiBottomSheetViewModel.requestTaxiBottomSheetUiState.onEach {
            when (it) {
                is RequestTaxiBottomSheetUiState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                RequestTaxiBottomSheetUiState.Loading -> Log.i("RequestTaxiBottomSheet", "Loading")
                is RequestTaxiBottomSheetUiState.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "${it.recommendedPrice}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        requestTaxiBottomSheetViewModel.requestTaxiBottomSheetUiState.onEach {
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun setEditTexts(vararg endTexts: EndTextEditText) {
        for (endText in endTexts) {
            endText.setOnActivatedListener { isActivated ->
                endText.setEndTextVisible(isActivated)
            }
            /*       endText.setOnTextChangedListener {
                       if (it.isNotEmpty()) {
                           binding.rvLocations.isVisible = true
                       } else {
                           locationItemAdapter.submitList(emptyList())
                       }
                   }*/
        }
    }
}