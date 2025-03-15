package com.aralhub.araltaxi.driver.orders.sheet

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.ModalBottomSheetGoingToPickUpBinding
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

    private var onClientPickedUp: (order: OrderItem?) -> Unit = {}
    fun setOnClientPickedUp(onPickedUp: (order: OrderItem?) -> Unit) {
        this.onClientPickedUp = onPickedUp
    }

    private var rideCanceledListener: (order: OrderItem?) -> Unit = {}
    fun setOnRideCanceledListener(onRideCanceled: (order: OrderItem?) -> Unit) {
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
                onClientPickedUp(order)
            }
        }
        binding.btnCancel.setOnClickListener {
            rideCanceledListener(order)
        }

        binding.btnNavigator.setOnClickListener {
            val longitude = order!!.locations.getOrNull(0)?.coordinates?.longitude
            val latitude = order!!.locations.getOrNull(0)?.coordinates?.latitude
            openNavigationChooser(requireContext(), latitude!!, longitude!!)
        }
    }

    private fun setupUI() = binding.apply {
        order = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("OrderDetail", OrderItem::class.java)
        } else {
            arguments?.getParcelable("OrderDetail")
        }
        Log.i(TAG, "setupUI: $order")
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

    private fun openNavigationChooser(context: Context, latitude: Double, longitude: Double) {
        val uriYandex = "yandexnavi://build_route_on_map?lat_to=${latitude}&lon_to=${longitude}"
        val intentYandex = Intent(Intent.ACTION_VIEW, Uri.parse(uriYandex))
        intentYandex.setPackage("ru.yandex.yandexnavi")

        val uriGoogle = Uri.parse("google.navigation:q=${latitude},${longitude}&mode=w")
        val intentGoogle = Intent(Intent.ACTION_VIEW, uriGoogle)
        intentGoogle.setPackage("com.google.android.apps.maps")

        val chooserIntent = Intent.createChooser(intentYandex, "Выберите навигатор")
        val arr = arrayOfNulls<Intent>(1)
        arr[0] = intentGoogle
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arr)

        val activities = context.packageManager.queryIntentActivities(chooserIntent, 0)
        if(activities.size>0){
            startActivity(chooserIntent)
        }else{
            //do sth..
        }
    }

    companion object {
        const val TAG = "GoingToPickUpModalBottomSheet"
    }
}