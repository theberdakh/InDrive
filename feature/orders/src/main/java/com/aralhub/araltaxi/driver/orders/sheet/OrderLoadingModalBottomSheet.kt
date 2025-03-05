package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.driver.orders.orders.OrdersViewModel
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.ModalBottomSheetOrderLoadingBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderLoadingModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_order_loading) {

    private val binding by viewBinding(ModalBottomSheetOrderLoadingBinding::bind)
    private val orderViewModel by activityViewModels<OrdersViewModel>()

    private val orderRejectedByClientModalBottomSheet = OrderRejectedByClientModalBottomSheet()

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        behavior.isHideable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObservers()

    }

    private fun initUI() {
        val offerAmount = arguments?.getString("OfferAmount")
        binding.tvOfferAmount.text = getString(R.string.driver_offer_loading, offerAmount)
    }

    private fun initObservers() {
        orderViewModel.rejectOfferState.onEach {
            orderRejectedByClientModalBottomSheet.arguments = arguments
            orderRejectedByClientModalBottomSheet.show(
                parentFragmentManager,
                orderRejectedByClientModalBottomSheet.tag
            )
            dismissAllowingStateLoss()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        const val TAG = "OrderLoadingModalBottomSheet"
    }
}