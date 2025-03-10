package com.aralhub.araltaxi.create_order

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.create_order.databinding.FragmentCreateOrderBinding
import com.aralhub.araltaxi.create_order.navigation.FeatureCreateOrderNavigation
import com.aralhub.araltaxi.create_order.utils.NewCurrentLocationListener
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
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
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
    private lateinit var mapWindow: MapWindow
    private var map: Map? = null

    @Inject
    lateinit var navigation: FeatureCreateOrderNavigation
    private val newCurrentLocationListener = NewCurrentLocationListener(
        onInitMapPosition = { point -> createPlaceMarkObject(point) },
        onUpdateMapPosition = { point -> updateMapPosition(point) },
        onProviderDisabledListener = { },
        onProviderEnabledListener = { }
    )
    private var placeMarkObject: PlacemarkMapObject? = null
    private var imageProvider: ImageProvider? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        initMap()
        initObservers()
        initViews()
        initListeners()
        initArgs()
        viewModel.setPaymentMethodType(PaymentMethodType.CASH)
        viewModel.getActivePaymentMethods()
        viewModel.getRideOptions()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun initMap() {
        mapWindow = binding.mapView.mapWindow
        map = mapWindow.map
        imageProvider =
            ImageProvider.fromResource(requireContext(), com.aralhub.ui.R.drawable.ic_vector)
    }

    override fun onResume() {
        super.onResume()
        locationManager?.let { observeLocationUpdates(it) }
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(newCurrentLocationListener)
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(locationManager: LocationManager) {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            newCurrentLocationListener
        )
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
       // MoneyFormatter(binding.etPrice)
    }

    private fun initListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            true
        ) {
            val result = Bundle().apply {
                putBoolean("cancel", true)
            }
            parentFragmentManager.setFragmentResult("cancel", result)
            findNavController().navigateUp()
        }



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
                    binding.etPrice.hint = "Esaplanıp atır..."
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

    private fun createPlaceMarkObject(point: Point) {
        map?.let {
            if (it.isValid) {
                placeMarkObject = it.mapObjects.addPlacemark().apply {
                    geometry = point
                    imageProvider?.let { imageProvider -> setIcon(imageProvider) }
                }
                it.move(CameraPosition(point, 17.0f, 150.0f, 30.0f))
            }
        }

    }

    private fun updateMapPosition(point: Point) {
        val cameraPosition = CameraPosition(point, 17.0f, 150.0f, 30.0f)
        placeMarkObject?.geometry = point
        map?.let {
            if (it.isValid) {
                it.move(cameraPosition)
            }
        }
    }


    private fun displayRecommendedPrice(recommendedPrice: RecommendedPrice) {
        binding.tvPrice.text = getString(
            com.aralhub.ui.R.string.placeholder_recommended_price,
            recommendedPrice.recommendedAmount.toInt()
        )
        val editable = Editable.Factory.getInstance()
            .newEditable("${recommendedPrice.recommendedAmount.toInt()}")
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