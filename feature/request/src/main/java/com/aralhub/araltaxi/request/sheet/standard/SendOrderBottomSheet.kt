package com.aralhub.araltaxi.request.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            commentToDriverModalBottomSheet.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }

    }
}