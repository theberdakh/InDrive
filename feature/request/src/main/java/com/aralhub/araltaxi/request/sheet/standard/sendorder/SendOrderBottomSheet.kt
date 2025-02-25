package com.aralhub.araltaxi.request.sheet.standard.sendorder

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.BottomSheetSendOrderBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.request.adapter.rideoption.RideOptionItemAdapter
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.araltaxi.request.sheet.modal.addlocation.AddLocationModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.ChangePaymentMethodModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.CommentToDriverModalBottomSheet
import com.aralhub.indrive.core.data.model.client.GeoPoint
import com.aralhub.indrive.core.data.model.payment.PaymentMethodType
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    }

    private fun initArgs() {
    }

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
            Log.i("SendOrderBottomSheet", "btnSendOffer: ${rideOptionItemAdapter.currentList}")
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
            Log.i("SendOrderBottomSheet", "Comment: $comment")
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
            Log.i("price", "price: $price")
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

    private fun initObservers() {
        viewModel.getRecommendedPrice(listOf(
            GeoPoint(
                latitude = 42.482461,
                longitude = 59.613237,
                name = "string"
            ), GeoPoint(
                latitude = 42.466078,
                longitude = 59.611981,
                name = "string"
            )
        ))
        viewModel.recommendedPriceUiState.onEach {
            when(it){
                is RecommendedPriceUiState.Error -> {
                    binding.tvPrice.text = "Usınıs etilgen bahanı esaplawda qa'telik"
                }
                RecommendedPriceUiState.Loading -> {
                    binding.tvPrice.text = "Esaplanıp atır..."
                }
                is RecommendedPriceUiState.Success -> {
                    binding.tvPrice.text = "Usınıs etilgen baha ${it.recommendedPrice.recommendedAmount.toInt()} som"
                    val editable = Editable.Factory.getInstance().newEditable("${it.recommendedPrice.recommendedAmount.toInt()}")
                    minimumPrice = it.recommendedPrice.minAmount.toInt()
                    maximumPrice = it.recommendedPrice.maxAmount.toInt()
                    binding.etPrice.text = editable
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.paymentMethod.onEach {
            when(it){
                PaymentMethodType.CARD -> {
                    binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_credit_card_3d)
                }
                PaymentMethodType.CASH -> {
                    binding.ivChangePaymentMethod.setImageResource(com.aralhub.ui.R.drawable.ic_cash)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.createRide()
        viewModel.sendOrderBottomSheetUiState.onEach {
            when(it){
                is SendOrderBottomSheetUiState.Error -> {}
                SendOrderBottomSheetUiState.Loading -> {}
                is SendOrderBottomSheetUiState.Success -> { Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show() }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getActivePaymentMethods()
        viewModel.activePaymentMethodUiState.onEach {
            when(it){
                is ActivePaymentMethodUiState.Error -> {}
                ActivePaymentMethodUiState.Loading -> {}
                is ActivePaymentMethodUiState.Success -> { Log.i("SendOrderBottomSheet", "ActivePaymentMethod: ${it.paymentMethods}") }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getRideOptions()
        viewModel.rideOptionsUiState.onEach {
            when(it){
                is RideOptionsUiState.Error -> {}
                RideOptionsUiState.Loading -> {}
                is RideOptionsUiState.Success -> { rideOptionItemAdapter.submitList(it.rideOptions) }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {
        fun args(geoPoints: List<GeoPoint>) = Bundle().apply {
            putSerializable("geoPoints", ArrayList(geoPoints))
        }
    }
}