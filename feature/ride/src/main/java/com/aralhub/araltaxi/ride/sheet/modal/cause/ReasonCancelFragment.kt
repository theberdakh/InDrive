package com.aralhub.araltaxi.ride.sheet.modal.cause

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.FragmentReasonCancelBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.ride.CancelRideUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.ui.adapter.CancelItemAdapter
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReasonCancelFragment: BottomSheetDialogFragment(R.layout.fragment_reason_cancel){
    private val binding by viewBinding(FragmentReasonCancelBinding::bind)
    private val adapter by lazy { CancelItemAdapter() }
    private val viewModel by viewModels<ReasonCancelViewModel>()
    private val rideViewModel by viewModels<RideViewModel>()
    @Inject lateinit var errorHandler: ErrorHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initObservers()
        viewModel.getCancelCauses()
    }

    private fun initListeners() {
        binding.btnSend.setOnClickListener {
            val reasonId = adapter.currentList.find { it.isSelected }?.id
            reasonId?.let { id ->
                rideViewModel.cancelRide(id)
            } ?: run {
                errorHandler.showToast("Please select a reason")
            }
        }
        adapter.setOnItemSelected { cause ->
            errorHandler.showToast(cause.title)
        }
    }

    private fun initViews() {
        binding.rvReasons.adapter = adapter
    }

    private fun initObservers() {
        observeState(rideViewModel.cancelRideState){ cancelRideUiState ->
            when(cancelRideUiState){
                is CancelRideUiState.Error -> {
                    errorHandler.showToast(cancelRideUiState.message)
                    Log.i("RideBottomSheet", "cancelRideUiState: Error ${cancelRideUiState.message}")
                }
                CancelRideUiState.Loading -> {}
                CancelRideUiState.Success -> {
                    Log.i("Reason cancel bottom sheet", "cancelRideUiState: Success")
                    dismissAllowingStateLoss()

                }
            }
        }
        observeState(viewModel.reasonCancelUiState){ reasonCancelUiState ->
            when(reasonCancelUiState){
                is ReasonCancelUiState.Success -> {
                    adapter.submitList(reasonCancelUiState.causes)
                }
                is ReasonCancelUiState.Error -> {
                    errorHandler.showToast(reasonCancelUiState.message)
                }
                ReasonCancelUiState.Loading -> {}
            }
        }
    }

    companion object {
        const val TAG = "ReasonCancelFragment"
    }
}