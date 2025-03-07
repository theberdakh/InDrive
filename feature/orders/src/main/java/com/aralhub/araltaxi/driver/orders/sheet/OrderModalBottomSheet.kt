package com.aralhub.araltaxi.driver.orders.sheet

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.driver.orders.orders.CreateOfferUiState
import com.aralhub.araltaxi.driver.orders.orders.OfferViewModel
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.ModalBottomSheetOrderBinding
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_order) {
    private val binding by viewBinding(ModalBottomSheetOrderBinding::bind)
    private val orderLoadingModalBottomSheet = OrderLoadingModalBottomSheet()

    private val offerViewModel by viewModels<OfferViewModel>()

    private var order: OrderItem? = null
    private var offerAmount = 0
    private var baseAmount = 0

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MoneyFormatter(binding.etPrice)
        setWebView()
        setupUI()
        setupListeners()
        initObservers()

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
        baseAmount = order?.roadPrice?.toInt() ?: 0
        order?.paymentType?.resId?.let { ivPaymentMethod.setImageResource(it) }
        Glide.with(binding.ivAvatar.context)
            .load(order?.avatar)
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivAvatar)
    }

    private fun setupListeners() {
        binding.btnSendOffer.setOnClickListener {
            offerAmount = binding.etPrice.text?.filter { it.isDigit() }.toString().toInt()
            if (order != null) {
                offerViewModel.createOffer(
                    order!!.id,
                    offerAmount
                )
            }
        }
        binding.btnAcceptOffer.setOnClickListener {
            binding.etPrice.text?.clear()
            if (order != null) {
                offerViewModel.createOffer(
                    order!!.id,
                    baseAmount
                )
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        binding.wvMap.apply {
            setBackgroundColor(0)
            // Enable JavaScript
            settings.javaScriptEnabled = true

            // Configure WebView settings
            settings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                useWideViewPort = true
                loadWithOverviewMode = true
            }

            // Set WebViewClient with override for error handling
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    Log.e(TAG, "WebView error: ${error?.description}")
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return true
                }
            }
            // Load the URL
            val startLat = 42.473810
            val startLong = 59.615314

            val endLat = 42.465140
            val endLong = 59.612868
            loadUrl("https://unique-banoffee-29f6df.netlify.app/?z=15&loc=$startLat%2C$startLong&loc=$endLat%2C$endLong&hl=ru&alt=0")
        }
    }

    private fun initObservers() {
        offerViewModel.createOfferUiState.onEach { result ->
            Log.i(TAG, "initObservers: $result")
            when (result) {
                is CreateOfferUiState.Error -> {
                    //show error
                }

                CreateOfferUiState.Loading -> {}
                is CreateOfferUiState.Success -> {
                    val bundle = Bundle()
                    val customOfferAmount = binding.etPrice.text.toString()
                    val offerAmount = customOfferAmount.ifEmpty {
                        getString(
                            com.aralhub.ui.R.string.standard_uzs_price, "$baseAmount"
                        )
                    }
                    bundle.putString("OfferAmount", offerAmount)
                    orderLoadingModalBottomSheet.arguments = bundle
                    orderLoadingModalBottomSheet.show(
                        parentFragmentManager,
                        OrderLoadingModalBottomSheet.TAG
                    )
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (orderLoadingModalBottomSheet.isAdded)
            orderLoadingModalBottomSheet.dismissAllowingStateLoss()
    }

    companion object {
        const val TAG = "OrderModalBottomSheet"
    }
}