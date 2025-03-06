package com.aralhub.araltaxi.driver.orders.sheet

import android.os.Build
import android.os.Bundle
import android.view.View
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.ModalBottomSheetGoingToPickUpBinding
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GoingToPickUpModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_going_to_pick_up) {
    private val binding by viewBinding(ModalBottomSheetGoingToPickUpBinding::bind)

    private var order: OrderItem? = null

    private var onClientPickedUp: () -> Unit = {}
    fun setOnClientPickedUp(onPickedUp: () -> Unit) {
        this.onClientPickedUp = onPickedUp
    }

    private var rideCanceledListener: () -> Unit = {}
    fun setOnRideCanceledListener(onRideCanceled: () -> Unit) {
        this.rideCanceledListener = onRideCanceled
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()

    }

    private fun setupListeners() {
        binding.slideArrived.setOnSlideChangeListener {
            if (it == 1f) {
                onClientPickedUp.invoke()
            }
        }
        binding.btnCancel.setOnClickListener {
            rideCanceledListener.invoke()
        }
    }

    private fun setupUI() = binding.apply {
        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("OrderDetail", OrderItem::class.java)
        } else {
            arguments?.getParcelable("OrderDetail")
        }
        tvPrice.text = getString(com.aralhub.ui.R.string.standard_uzs_price, order?.roadPrice)
        tvClientName.text = order?.name
        tvDistance.text = order?.roadDistance
        tvDistanceToClient.text = order?.pickUpDistance
        tvFromLocation.text = order?.pickUpAddress
        tvToLocation.text = order?.destinationAddress
        order?.paymentType?.resId?.let { ivPaymentMethod.setImageResource(it) }
        Glide.with(binding.ivAvatar.context)
            .load(order?.avatar)
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivAvatar)
    }


    companion object {
        const val TAG = "GoingToPickUpModalBottomSheet"
    }
}