package com.aralhub.araltaxi.request.sheet.standard.sendorder

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.BottomSheetSendOrderBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.araltaxi.request.sheet.modal.ChangePaymentMethodModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.CommentToDriverModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.addlocation.AddLocationModalBottomSheet
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.client.RecommendedPrice
import com.aralhub.indrive.core.data.model.payment.PaymentMethodType
import com.aralhub.ui.adapter.option.RideOptionItemAdapter
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendOrderBottomSheet : Fragment(R.layout.bottom_sheet_send_order) {
    @Inject lateinit var featureRequestNavigation: FeatureRequestNavigation
    @Inject lateinit var bottomSheetNavigation: FeatureRequestBottomSheetNavigation
    @Inject lateinit var errorHandler: ErrorHandler
    private val binding by viewBinding(BottomSheetSendOrderBinding::bind)
    private var isConfiguring: Boolean = false
    private val changePaymentMethodModalBottomSheet by lazy { ChangePaymentMethodModalBottomSheet() }
    private val commentToDriverModalBottomSheet by lazy { CommentToDriverModalBottomSheet() }
    private val viewModel by viewModels<SendOrderBottomSheetViewModel>()
    private var minimumPrice = 0
    private var maximumPrice = 0
    private var comment = ""
    private val rideOptionItemAdapter by lazy { RideOptionItemAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArgs()
        initObservers()
        initViews()
        initListeners()
        viewModel.getActivePaymentMethods()
        viewModel.getRecommendedPrice(listOf(GeoPoint(latitude = 42.474078, longitude = 59.615902, name = "string"), GeoPoint(latitude = 42.463283, longitude = 59.605034, name = "string")))
        viewModel.createRide()
        viewModel.getRideOptions()
    }

    private fun initArgs() {}

    private fun initViews() {
        binding.rvRideOptions.adapter = rideOptionItemAdapter
    }

    private fun initListeners() {
        MoneyFormatter(binding.etPrice)
        binding.ivConfigure.setOnClickListener {
            isConfiguring = !isConfiguring
            binding.layoutConfigure.isVisible = isConfiguring
        }

        binding.btnSendOffer.setOnClickListener {
          //  featureRequestNavigation.goToGetOffersFromSendOrderFragment()
            val enabledOptionIds = rideOptionItemAdapter.currentList.filter { it.isEnabled }.map { it.id }
        }

        initChangePaymentMethodListener()
        initCommentToDriverListener()
        binding.ivChangePaymentMethod.setOnClickListener {
            changePaymentMethodModalBottomSheet.show(requireActivity().supportFragmentManager, ChangePaymentMethodModalBottomSheet.TAG)
        }
        binding.iconAdd.setOnClickListener {
            AddLocationModalBottomSheet().show(requireActivity().supportFragmentManager, "")
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
        when(paymentMethod){
            PaymentMethodType.CARD -> binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_credit_card_3d)
            PaymentMethodType.CASH -> binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_cash)
        }
    }

    private fun initObservers() {
        observeState(viewModel.paymentMethod){ paymentMethod -> displayPaymentMethod(paymentMethod) }
        observeState(viewModel.recommendedPriceUiState){ recommendedPriceUiState ->
            when(recommendedPriceUiState){
                is RecommendedPriceUiState.Error -> errorHandler.showToast(recommendedPriceUiState.message)
                RecommendedPriceUiState.Loading -> {}
                is RecommendedPriceUiState.Success -> displayRecommendedPrice(recommendedPriceUiState.recommendedPrice)
            }
        }
        observeState(viewModel.sendOrderBottomSheetUiState){ sendOrderBottomSheetUiState ->
            when(sendOrderBottomSheetUiState){
                is SendOrderBottomSheetUiState.Error -> errorHandler.showToast(sendOrderBottomSheetUiState.message)
                SendOrderBottomSheetUiState.Loading -> {}
                is SendOrderBottomSheetUiState.Success -> featureRequestNavigation.goToGetOffersFromSendOrderFragment()
            }
        }
        observeState(viewModel.rideOptionsUiState){ rideOptionsUiState ->
            when(rideOptionsUiState){
                is RideOptionsUiState.Error -> errorHandler.showToast(rideOptionsUiState.message)
                RideOptionsUiState.Loading -> {}
                is RideOptionsUiState.Success -> rideOptionItemAdapter.submitList(rideOptionsUiState.rideOptions)
            }
        }
    }

    private fun displayRecommendedPrice(recommendedPrice: RecommendedPrice) {
        binding.tvPrice.text = getString(R.string.placeholder_recommended_price, recommendedPrice.recommendedAmount.toInt())
        val editable = Editable.Factory.getInstance().newEditable("${recommendedPrice.recommendedAmount.toInt()}")
        minimumPrice = recommendedPrice.minAmount.toInt()
        maximumPrice = recommendedPrice.maxAmount.toInt()
        binding.etPrice.text = editable
    }

    companion object {
        fun args(geoPoints: List<GeoPoint>) = Bundle().apply {
            putSerializable("geoPoints", ArrayList(geoPoints))
        }
    }
}