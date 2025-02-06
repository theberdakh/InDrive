package com.aralhub.indrive.waiting.modal

import android.os.Bundle
import android.view.View
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentTripCancelledByDriverBinding
import com.aralhub.indrive.waiting.navigation.FeatureWaitingNavigation
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TripCanceledByDriverFragment: BottomSheetDialogFragment(R.layout.fragment_trip_cancelled_by_driver) {
    private val binding by viewBinding(FragmentTripCancelledByDriverBinding::bind)
    @Inject
    lateinit var waitingNavigation: FeatureWaitingNavigation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnClear.setOnClickListener {
            dismissAllowingStateLoss()
            waitingNavigation.goBackToHomeFragment()
        }
    }

    companion object {
        const val TAG = "TripCanceledByDriverFragment"
    }
}