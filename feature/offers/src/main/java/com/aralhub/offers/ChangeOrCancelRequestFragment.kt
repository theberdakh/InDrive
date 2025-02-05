package com.aralhub.offers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aralhub.offers.databinding.FragmentChangeOrCancelRequestBinding
import com.aralhub.offers.modal.CancelRequestBottomSheetDialogFragment
import com.aralhub.offers.navigation.sheet.SheetNavigator
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeOrCancelRequestFragment : Fragment(R.layout.fragment_change_or_cancel_request) {
    private val binding by viewBinding(FragmentChangeOrCancelRequestBinding::bind)
    @Inject
    lateinit var sheetNavigator: SheetNavigator
    private val cancelRequestBottomSheetDialogFragment by lazy { CancelRequestBottomSheetDialogFragment() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            cancelRequestBottomSheetDialogFragment.show(childFragmentManager, "cancel_request")
        }

        binding.btnChange.setOnClickListener {
            sheetNavigator.goToChangePriceFragment()
        }
    }
}