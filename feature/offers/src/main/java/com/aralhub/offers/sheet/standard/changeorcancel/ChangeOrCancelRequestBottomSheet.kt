package com.aralhub.offers.sheet.standard.changeorcancel

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aralhub.araltaxi.client.offers.R
import com.aralhub.araltaxi.client.offers.databinding.BottomSheetChangeOrCancelRequestBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.offers.navigation.FeatureOffersNavigation
import com.aralhub.offers.navigation.sheet.FeatureOffersBottomSheetNavigation
import com.aralhub.offers.sheet.modal.ReasonCancelModalBottomSheet
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeOrCancelRequestBottomSheet : Fragment(R.layout.bottom_sheet_change_or_cancel_request) {
    private val binding by viewBinding(BottomSheetChangeOrCancelRequestBinding::bind)
    @Inject lateinit var featureOffersBottomSheetNavigation: FeatureOffersBottomSheetNavigation
    @Inject lateinit var errorHandler: ErrorHandler
    private var searchRideId = SEARCH_ID_DEFAULT
    @Inject lateinit var navigation: FeatureOffersNavigation
    private val reasonCancelModalBottomSheet by lazy { ReasonCancelModalBottomSheet() }
    private val viewModel by viewModels<ChangeOrCancelRequestViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
        viewModel.getSearchRide(39)
    }

    private fun initObservers() {
        observeState(viewModel.cancelSearchRideUiState){ cancelSearchRideUiState ->
            when(cancelSearchRideUiState){
                is CancelSearchRideUiState.Error -> errorHandler.showToast(cancelSearchRideUiState.message)
                CancelSearchRideUiState.Loading -> {}
                CancelSearchRideUiState.Success -> {
                    errorHandler.showToast("Offer canceled")
                    navigation.goBackToRequestFragmentFromOffersFragment()
                }
            }
        }
        observeState(viewModel.searchRideUiState){ searchRideUiState ->
            when(searchRideUiState){
                is SearchRideUiState.Error -> errorHandler.showToast(searchRideUiState.message)
                SearchRideUiState.Loading -> {}
                is SearchRideUiState.Success -> { searchRideId = searchRideUiState.searchRide.uuid }
            }
        }
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            viewModel.cancelSearchRide(39)
        }

        binding.btnCancel.setOnClickListener {
            viewModel.cancelSearchRide(39)
        }

        binding.btnChange.setOnClickListener {
            if (searchRideId != SEARCH_ID_DEFAULT) {
                featureOffersBottomSheetNavigation.goToChangePriceFragment(searchRideId)
            } else {
                errorHandler.showToast("Ride is not available")
            }
        }
    }

    companion object {
        private const val SEARCH_ID_DEFAULT = "searchId"
    }
}