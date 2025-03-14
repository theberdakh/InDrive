package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetCancelTripBinding
import com.aralhub.araltaxi.driver.orders.orders.OrdersViewModel
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CancelTripModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_cancel_trip) {

    private val binding by viewBinding(ModalBottomSheetCancelTripBinding::bind)

    private val viewModel by viewModels<OrdersViewModel>()

    private val reasonCancelModalBottomSheet = ReasonCancelModalBottomSheet()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()

    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            val rideId = arguments?.getInt("rideId")
            rideId?.let { id -> viewModel.cancelRide(id, 2) }
        }
    }

    private fun setupObservers() {
        viewModel.rideCanceledResult.onEach { rideId ->
            dismissAllowingStateLoss()
            reasonCancelModalBottomSheet.show(
                childFragmentManager,
                ReasonCancelModalBottomSheet.TAG
            )
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        const val TAG = "CancelTripModalBottomSheet"
    }
}