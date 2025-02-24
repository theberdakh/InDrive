package com.aralhub.araltaxi.request.sheet.standard.sendorder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.BottomSheetSendOrderBinding
import com.aralhub.araltaxi.request.navigation.FeatureRequestNavigation
import com.aralhub.araltaxi.request.navigation.sheet.FeatureRequestBottomSheetNavigation
import com.aralhub.araltaxi.request.sheet.modal.addlocation.AddLocationModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.ChangePaymentMethodModalBottomSheet
import com.aralhub.araltaxi.request.sheet.modal.CommentToDriverModalBottomSheet
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SendOrderBottomSheet : Fragment(R.layout.bottom_sheet_send_order) {
    @Inject
    lateinit var featureRequestNavigation: FeatureRequestNavigation

    @Inject
    lateinit var bottomSheetNavigation: FeatureRequestBottomSheetNavigation
    private val binding by viewBinding(BottomSheetSendOrderBinding::bind)
    private var isConfiguring: Boolean = false
    private val changePaymentMethodModalBottomSheet by lazy { ChangePaymentMethodModalBottomSheet() }
    private val commentToDriverModalBottomSheet by lazy { CommentToDriverModalBottomSheet() }
    private val viewModel by viewModels<SendOrderBottomSheetViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        MoneyFormatter(binding.etPrice)
        binding.ivConfigure.setOnClickListener {
            isConfiguring = !isConfiguring
            binding.layoutConfigure.isVisible = isConfiguring
        }

        binding.btnSendOffer.setOnClickListener {
            featureRequestNavigation.goToGetOffersFromSendOrderFragment()
        }

        binding.ivChangePaymentMethod.setOnClickListener {
            changePaymentMethodModalBottomSheet.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
        binding.iconAdd.setOnClickListener {
            AddLocationModalBottomSheet().show(requireActivity().supportFragmentManager, "")
        }

        binding.layoutCommentToDriver.setOnClickListener {
            commentToDriverModalBottomSheet.show(requireActivity().supportFragmentManager, "")
        }

    }

    private fun initObservers() {
        viewModel.createRide()
        viewModel.sendOrderBottomSheetUiState.onEach {
            when(it){
                is SendOrderBottomSheetUiState.Error -> {}
                SendOrderBottomSheetUiState.Loading -> {}
                is SendOrderBottomSheetUiState.Success -> { Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show() }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.getActivePaymentMethod()
        viewModel.activePaymentMethodUiState.onEach {
            when(it){
                is ActivePaymentMethodUiState.Error -> {}
                ActivePaymentMethodUiState.Loading -> {}
                is ActivePaymentMethodUiState.Success -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getRideOptions()
        viewModel.rideOptionsUiState.onEach {
            when(it){
                is RideOptionsUiState.Error -> {}
                RideOptionsUiState.Loading -> {}
                is RideOptionsUiState.Success -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}