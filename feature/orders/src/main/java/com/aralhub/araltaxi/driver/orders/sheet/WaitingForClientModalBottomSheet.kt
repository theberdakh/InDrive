package com.aralhub.araltaxi.driver.orders.sheet

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetWaitingForClientBinding
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WaitingForClientModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_waiting_for_client) {
    private val binding by viewBinding(ModalBottomSheetWaitingForClientBinding::bind)

    private var order: OrderItem? = null

    private var timer: CountDownTimer? = null
    private var remainingTime = 5L

    private var onGoingToRideListener: (order: OrderItem?) -> Unit = {}
    fun setOnGoingToRideListener(onGoingToRide: (order: OrderItem?) -> Unit) {
        this.onGoingToRideListener = onGoingToRide
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
        startTimer()

    }

    private fun setupListeners() {
        binding.btnGoToRide.setOnClickListener {
            onGoingToRideListener(order)
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

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(remainingTime * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished / 1000
                binding.tvTime.text = formatTime(remainingTime)
            }

            override fun onFinish() {
                binding.tvTime.setTextColor(ContextCompat.getColor(requireContext(), com.aralhub.ui.R.color.color_status_error_2))
                binding.tvTimerLabel.text = getString(com.aralhub.ui.R.string.label_paid_waiting_time_started)
                startPaidWaitingTimer()
            }
        }.start()
    }

    private fun startPaidWaitingTimer() {
        var paidWaitingTime = 0L // Время платного ожидания

        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) { // Бесконечный таймер
            override fun onTick(millisUntilFinished: Long) {
                paidWaitingTime++
                binding.tvTime.text = formatTime(paidWaitingTime)
            }

            override fun onFinish() {}
        }.start()
    }

    private fun formatTime(timeInSeconds: Long): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Останавливаем таймер при выходе
    }

    companion object {
        const val TAG = "WaitingForClientModalBottomSheet"
    }
}