package com.aralhub.araltaxi.driver.orders.sheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetReasonCancelBinding
import com.aralhub.araltaxi.driver.orders.orders.CancelRideUiState
import com.aralhub.araltaxi.driver.orders.orders.OrdersViewModel
import com.aralhub.araltaxi.driver.orders.orders.RideCancelUiState
import com.aralhub.ui.adapter.CancelItemAdapter
import com.aralhub.ui.adapter.DriverCancelItemAdapter
import com.aralhub.ui.dialog.ErrorMessageDialog
import com.aralhub.ui.dialog.LoadingDialog
import com.aralhub.ui.model.CancelItem
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ReasonCancelModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_reason_cancel) {

    private val binding by viewBinding(ModalBottomSheetReasonCancelBinding::bind)

    private val viewModel by activityViewModels<OrdersViewModel>()

    private var errorDialog: ErrorMessageDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private val adapter = DriverCancelItemAdapter()

    private var cancelCauseId: Int? = null

    private var listOfCancelCauses = mutableListOf<CancelItem>()

    private var action: () -> Unit = {}
    fun setOnRideCancelledListener(action: () -> Unit) {
        this.action = action
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        errorDialog = ErrorMessageDialog(context)
        loadingDialog = LoadingDialog(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        fetchData()
        setupListeners()
        setupObservers()

    }

    private fun setupData() {
        binding.rvReasons.adapter = adapter

        cancelCauseId?.let { binding.btnSend.isEnabled = true }
    }

    private fun fetchData() {
        viewModel.getCancelCauses()
    }

    private fun setupListeners() {

        binding.btnSend.setOnClickListener {
            Timber.w("cancelCauseId: $cancelCauseId \nrideId: ${arguments?.getInt("rideId")}")
            val rideId = arguments?.getInt("rideId")
            if (rideId != null && cancelCauseId != null)
                viewModel.cancelRide(rideId, cancelCauseId!!)
        }

        adapter.setOnItemSelected { item, isChecked ->
            cancelCauseId = item.id
            binding.btnSend.isEnabled = isChecked

            val list = listOfCancelCauses.map {
                if (it.id == item.id) item.copy(isSelected = true)
                else it.copy(isSelected = false)
            }
            adapter.submitList(list)
        }
    }

    private fun setupObservers() {

        viewModel.getCancelCausesResult.onEach { result ->
            when (result) {
                is CancelRideUiState.Error -> {
                    showErrorDialog(result.message)
                }

                CancelRideUiState.Loading -> {
                    showLoading()
                }

                is CancelRideUiState.Success -> {
                    dismissLoading()
                    listOfCancelCauses.clear()
                    listOfCancelCauses.addAll(result.data)
                    adapter.submitList(listOfCancelCauses)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.rideCanceledResult.onEach { result ->
            when (result) {
                is RideCancelUiState.Error -> {
                    showErrorDialog(result.message)
                }

                RideCancelUiState.Loading -> {
                    showLoading()
                }

                RideCancelUiState.Success -> {
                    dismissLoading()
                    action.invoke()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showErrorDialog(errorMessage: String?) {
        dismissLoading()
        errorDialog?.show(errorMessage)

        errorDialog?.setOnDismissClicked { errorDialog?.dismiss() }
    }

    private fun showLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            loadingDialog?.show()
        }
    }

    private fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    private fun dismissErrorDialog() {
        errorDialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissErrorDialog()
        dismissLoading()
    }

    companion object {
        const val TAG = "ReasonCancelModalBottomSheet"
    }
}