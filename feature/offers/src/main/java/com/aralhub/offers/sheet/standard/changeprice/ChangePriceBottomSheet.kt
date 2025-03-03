package com.aralhub.offers.sheet.standard.changeprice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.client.offers.R
import com.aralhub.araltaxi.client.offers.databinding.BottomSheetChangePriceBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePriceBottomSheet: Fragment(R.layout.bottom_sheet_change_price) {
    private val binding by viewBinding(BottomSheetChangePriceBinding::bind)
    @Inject lateinit var errorHandler: ErrorHandler
    private var searchRideId = ARG_RIDE_ID_DEFAULT
    private val viewModel by viewModels<ChangePriceViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moneyFormatter = MoneyFormatter(binding.etPrice)
        initArgs()
        initListeners()
        initObservers()
    }

    private fun initArgs() {
        searchRideId = requireArguments().getString(ARG_RIDE_ID, ARG_RIDE_ID_DEFAULT) ?: ARG_RIDE_ID_DEFAULT
    }

    private fun initListeners() {
       binding.btnChange.setOnClickListener {
           viewModel.changePrice(searchRideId, MoneyFormatter(binding.etPrice).getUnformattedText().toInt())
       }
    }

    private fun initObservers() {
        observeState(viewModel.changePriceUiState){ changePriceUiState ->
            when(changePriceUiState){
                is ChangePriceUiState.Error -> errorHandler.showToast(changePriceUiState.message)
                ChangePriceUiState.Loading -> {}
                is ChangePriceUiState.Success -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

    companion object {
        private const val ARG_RIDE_ID = "rideId"
        private const val ARG_RIDE_ID_DEFAULT = "rideId"
        fun args(searchRideId: String) = Bundle().apply {
            putString(ARG_RIDE_ID, searchRideId)
        }
    }
}