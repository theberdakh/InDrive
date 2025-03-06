package com.aralhub.araltaxi.create_order

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.create_order.databinding.FragmentCreateOrderBinding
import com.aralhub.araltaxi.create_order.navigation.FeatureCreateOrderNavigation
import com.aralhub.araltaxi.create_order.utils.CurrentLocationListener
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.model.payment.PaymentMethodType
import com.aralhub.indrive.core.data.model.ride.RecommendedAmount
import com.aralhub.ui.adapter.option.RideOptionItemAdapter
import com.aralhub.ui.model.args.SelectedLocations
import com.aralhub.ui.sheets.ChangePaymentMethodModalBottomSheet
import com.aralhub.ui.sheets.CommentToDriverModalBottomSheet
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateOrderFragment : Fragment(R.layout.fragment_create_order) {
    private val binding by viewBinding(FragmentCreateOrderBinding::bind)
    private var isConfiguring: Boolean = false
    @Inject
    lateinit var errorHandler: ErrorHandler
    private val changePaymentMethodModalBottomSheet by lazy { ChangePaymentMethodModalBottomSheet() }
    private val commentToDriverModalBottomSheet by lazy { CommentToDriverModalBottomSheet() }
    private var minimumPrice = 0
    private var maximumPrice = 0
    private var comment = ""
    private var recommendedPrice: RecommendedPrice? = null
    private val rideOptionItemAdapter by lazy { RideOptionItemAdapter() }
    private var enabledOptionsIds: MutableList<Int> = mutableListOf()
    private val viewModel by viewModels<CreateOrderViewModel>()
    private var selectedLocations: SelectedLocations? = null
    private var locationManager: LocationManager? = null
    @Inject lateinit var navigation: FeatureCreateOrderNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.let { observeLocationUpdates(it) }

        initObservers()
        initViews()
        initListeners()
        initArgs()
        viewModel.setPaymentMethodType(PaymentMethodType.CASH)
        viewModel.getActivePaymentMethods()
        viewModel.getRideOptions()
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(locationManager: LocationManager) {
        val lastKnownLocation =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: Location(
                LocationManager.GPS_PROVIDER
            ).apply {
                latitude = 42.4651
                longitude = 59.6136
            }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            CurrentLocationListener(requireContext(), binding.mapView.mapWindow.map,
                initialLocation = lastKnownLocation,
                onProviderEnabledListener = {
                    //enabled
                },
                onProviderDisabledListener = {
                    //disabled
                }))
    }

    private fun initArgs() {
        selectedLocations = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable<SelectedLocations>(
                "selectedLocations",
                SelectedLocations::class.java
            )
        } else {
            requireArguments().getParcelable("selectedLocations")
        }
        selectedLocations?.let {
            viewModel.getRecommendedPrice(
                listOf(
                    GeoPoint(
                        latitude = it.from.latitude,
                        longitude = it.from.longitude,
                        name = it.from.name
                    ),
                    GeoPoint(
                        latitude = it.to.latitude,
                        longitude = it.to.longitude,
                        name = it.to.name
                    )
                )
            )
            binding.tvFromLocationName.text = it.from.name
            binding.tvToLocationName.text = it.to.name
        }


    }

    private fun initViews() {
        binding.rvRideOptions.adapter = rideOptionItemAdapter

    }

    private fun initListeners() {
        MoneyFormatter(binding.etPrice)
        initChangePaymentMethodListener()
        initCommentToDriverListener()

        binding.ivConfigure.setOnClickListener {
            isConfiguring = !isConfiguring
            binding.layoutConfigure.isVisible = isConfiguring
        }

        binding.btnSendOffer.setOnClickListener {

            enabledOptionsIds.addAll(rideOptionItemAdapter.currentList.filter { it.isEnabled }
                .map { it.id })
            recommendedPrice?.let {
                viewModel.createRide(
                    baseAmount = binding.etPrice.text.toString().replace(" ", "").toInt(),
                    recommendedAmount = RecommendedAmount(
                        it.minAmount,
                        it.maxAmount,
                        it.recommendedAmount
                    ),
                    selectedLocations = selectedLocations!!,
                    comment = comment,
                    paymentId = viewModel.paymentMethod.value.id,
                    options = enabledOptionsIds
                )
            }

        }

        binding.ivChangePaymentMethod.setOnClickListener {
            changePaymentMethodModalBottomSheet.show(
                requireActivity().supportFragmentManager,
                ChangePaymentMethodModalBottomSheet.TAG
            )
        }

        binding.layoutCommentToDriver.setOnClickListener {
            commentToDriverModalBottomSheet.show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun initCommentToDriverListener() {
        commentToDriverModalBottomSheet.setOnSaveCommentToDriver {
            comment = it
            commentToDriverModalBottomSheet.dismissAllowingStateLoss()
        }
    }

    private fun initChangePaymentMethodListener() {
        changePaymentMethodModalBottomSheet.setOnCashClickListener {
            changePaymentMethodModalBottomSheet.dismissAllowingStateLoss()
            viewModel.setPaymentMethodType(PaymentMethodType.CASH)
        }

        changePaymentMethodModalBottomSheet.setOnOnlineClickListener {
            changePaymentMethodModalBottomSheet.dismissAllowingStateLoss()
            viewModel.setPaymentMethodType(PaymentMethodType.CARD)
        }

        binding.tvDecrease500.setOnClickListener {
            val price = Integer.parseInt(binding.etPrice.text.toString().replace(" ", ""))
            if (price - 500 >= minimumPrice) {
                val editable = Editable.Factory.getInstance().newEditable("${price - 500}")
                binding.etPrice.text = editable
            } else {
                errorHandler.showToast("Minimum price: $minimumPrice")
            }
        }
        binding.tvIncrease500.setOnClickListener {
            val price = Integer.parseInt(binding.etPrice.text.toString().replace(" ", ""))
            if (price + 500 <= maximumPrice) {
                val editable = Editable.Factory.getInstance().newEditable("${price + 500}")
                binding.etPrice.text = editable
            } else {
                errorHandler.showToast("Maximum price: $maximumPrice")
            }
        }
    }

    private fun displayPaymentMethod(paymentMethod: PaymentMethodType) {
        when (paymentMethod) {
            PaymentMethodType.CARD -> {
                binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_credit_card_3d)
            }

            PaymentMethodType.CASH -> {
                binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_cash)
            }
        }
    }

    private fun initObservers() {
        observeState(viewModel.paymentMethod) { paymentMethod -> displayPaymentMethod(paymentMethod) }
        observeState(viewModel.recommendedPriceUiState) { recommendedPriceUiState ->
            when (recommendedPriceUiState) {
                is RecommendedPriceUiState.Error -> errorHandler.showToast(recommendedPriceUiState.message)
                RecommendedPriceUiState.Loading -> {
                    binding.etPrice.setText("Esaplanıp atır...")
                }
                is RecommendedPriceUiState.Success -> {
                    displayRecommendedPrice(
                        recommendedPriceUiState.recommendedPrice
                    )
                    recommendedPrice = recommendedPriceUiState.recommendedPrice
                }
            }
        }
        observeState(viewModel.sendOrderBottomSheetUiState) { sendOrderBottomSheetUiState ->
            when (sendOrderBottomSheetUiState) {
                is SendOrderBottomSheetUiState.Error -> errorHandler.showToast(
                    sendOrderBottomSheetUiState.message
                )

                SendOrderBottomSheetUiState.Loading -> {}
                is SendOrderBottomSheetUiState.Success -> {
                   navigation.goToOffersFromCreateOrderFragment()
                }
            }
        }
        observeState(viewModel.rideOptionsUiState) { rideOptionsUiState ->
            when (rideOptionsUiState) {
                is RideOptionsUiState.Error -> errorHandler.showToast(rideOptionsUiState.message)
                RideOptionsUiState.Loading -> {}
                is RideOptionsUiState.Success -> rideOptionItemAdapter.submitList(rideOptionsUiState.rideOptions)
            }
        }
    }

    private fun displayRecommendedPrice(recommendedPrice: RecommendedPrice) {
        binding.tvPrice.text = getString(
            com.aralhub.ui.R.string.placeholder_recommended_price,
            recommendedPrice.recommendedAmount.toInt()
        )
        val editable = Editable.Factory.getInstance().newEditable("${recommendedPrice.recommendedAmount.toInt()}")
        minimumPrice = recommendedPrice.minAmount.toInt()
        maximumPrice = recommendedPrice.maxAmount.toInt()
        binding.etPrice.text = editable
    }

    companion object {
        fun args(selectedLocations: SelectedLocations) = Bundle().apply {
            putParcelable("selectedLocations", selectedLocations)
        }
    }
}