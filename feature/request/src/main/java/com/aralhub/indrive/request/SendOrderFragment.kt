package com.aralhub.indrive.request

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aralhub.indrive.request.databinding.FragmentSendOrderBinding
import com.aralhub.indrive.request.modal.AddLocationModalBottomSheet
import com.aralhub.indrive.request.modal.ChangePaymentMethodBottomSheetFragment
import com.aralhub.indrive.request.modal.CommentToDriverBottomSheetDialogFragment
import com.aralhub.indrive.request.navigation.FeatureRequestBottomSheetNavigation
import com.aralhub.indrive.request.navigation.FeatureRequestNavigation
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendOrderFragment : Fragment(R.layout.fragment_send_order) {
    @Inject
    lateinit var featureRequestNavigation: FeatureRequestNavigation

    @Inject
    lateinit var bottomSheetNavigation: FeatureRequestBottomSheetNavigation
    private val binding by viewBinding(FragmentSendOrderBinding::bind)
    private var isConfiguring: Boolean = false
    private val changePaymentMethodBottomSheetFragment by lazy { ChangePaymentMethodBottomSheetFragment() }
    private val commentToDriverBottomSheetDialogFragment by lazy { CommentToDriverBottomSheetDialogFragment() }
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
            changePaymentMethodBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
        binding.iconAdd.setOnClickListener {
            AddLocationModalBottomSheet().show(requireActivity().supportFragmentManager, "")
        }

        binding.layoutCommentToDriver.setOnClickListener {
            commentToDriverBottomSheetDialogFragment.show(
                requireActivity().supportFragmentManager,
                ""
            )
        }

    }
}