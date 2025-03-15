package com.aralhub.araltaxi.ride.sheet.standard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.client.ride.databinding.BottomSheetRideBinding
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.ride.ActiveRideUiState
import com.aralhub.araltaxi.ride.PaymentMethod
import com.aralhub.araltaxi.ride.Ride
import com.aralhub.araltaxi.ride.RideBottomSheetUiState
import com.aralhub.araltaxi.ride.RideState
import com.aralhub.araltaxi.ride.RideStateUiState
import com.aralhub.araltaxi.ride.RideViewModel
import com.aralhub.araltaxi.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.araltaxi.ride.sheet.modal.CancelTripFragment
import com.aralhub.araltaxi.ride.utils.FragmentEx.loadAvatar
import com.aralhub.araltaxi.ride.utils.FragmentEx.sendPhoneNumberToDial
import com.aralhub.indrive.core.data.model.ride.RideStatus
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideBottomSheet : Fragment(R.layout.bottom_sheet_ride) {
    @Inject
    lateinit var navigation: FeatureRideBottomSheetNavigation
    private val rideViewModel: RideViewModel by activityViewModels()
    @Inject lateinit var errorHandler: ErrorHandler
    private val binding by viewBinding(BottomSheetRideBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        //rideViewModel.getActiveRide()
    }

    private fun initObservers() {

    }

    private fun initRideData(rideData: Ride) {
        binding.tvTitle.text = "Aydawshı ~${rideData.waitForDriverTime} minut ishinde jetip keledi"
        loadAvatar(url = rideData.driver.avatar, binding.ivDriver)
        binding.tvDriverRating.text =
            getString(com.aralhub.ui.R.string.label_driver_rating, rideData.driver.rating)
        binding.tvDriverName.text = rideData.driver.name
        binding.tvCarInfo.text = StringUtils.getBoldSpanString(
            fullText = "${rideData.car.model}, ${rideData.car.number}",
            boldText = rideData.car.number, boldTextColorHex = "#2C2D2E"
        )
        binding.tvPrice.text = rideData.price
        binding.tvToLocation.text = rideData.route.end
        binding.tvPaymentMethod.text = when (rideData.paymentMethod) {
            PaymentMethod.CASH -> getString(com.aralhub.ui.R.string.label_cash)
            PaymentMethod.CARD -> getString(com.aralhub.ui.R.string.label_online_payment)
        }
        binding.btnCall.setOnClickListener {
            sendPhoneNumberToDial(rideData.driver.phone)
        }
        binding.btnCancel.setOnClickListener {
            CancelTripFragment().show(childFragmentManager, CancelTripFragment.TAG)
        }

    }
}