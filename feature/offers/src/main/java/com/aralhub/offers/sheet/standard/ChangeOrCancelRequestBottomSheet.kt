package com.aralhub.offers.sheet.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.offers.R
import com.aralhub.offers.databinding.BottomSheetChangeOrCancelRequestBinding
import com.aralhub.offers.navigation.sheet.FeatureOffersBottomSheetNavigation
import com.aralhub.offers.navigation.sheet.SheetNavigatorImpl
import com.aralhub.offers.sheet.modal.CancelRequestModalBottomSheet
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeOrCancelRequestBottomSheet : Fragment(R.layout.bottom_sheet_change_or_cancel_request) {
    private val binding by viewBinding(BottomSheetChangeOrCancelRequestBinding::bind)
    @Inject
    lateinit var navigation: FeatureOffersBottomSheetNavigation
    private val cancelRequestModalBottomSheet by lazy { CancelRequestModalBottomSheet() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            cancelRequestModalBottomSheet.show(childFragmentManager, "cancel_request")
        }

        binding.btnChange.setOnClickListener {
            navigation.goToChangePriceFragment()
        }
    }
}