package com.aralhub.indrive.ride

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aralhub.indrive.ride.modal.CancelTripFragment
import com.aralhub.indrive.ride.modal.TripCanceledByDriverFragment
import com.aralhub.indrive.ride.modal.WaitingTimeFragment
import com.aralhub.indrive.ride.navigation.sheet.FeatureRideBottomSheetNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.BottomSheetRideBinding
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.ViewEx.hide
import com.aralhub.ui.utils.ViewEx.show
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideBottomSheet : Fragment(R.layout.bottom_sheet_ride) {
    private val waitingCountDownTimer = WaitingCountDownTimer(
        onCountDownTimerTick = ::onCountDownTimerTick,
        onCountDownTimerFinish = ::onCountDownTimerFinish
    )
    private fun onCountDownTimerFinish() {}
    private fun onCountDownTimerTick(minutes: Long, seconds: Long) {
        binding.tvTime.text = "$minutes:$seconds"
    }
    @Inject
    lateinit var bottomSheetNavigation: FeatureRideBottomSheetNavigation
    private val rideBottomSheetViewModel: RideBottomSheetViewModel by viewModels()
    private val binding by viewBinding(BottomSheetRideBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setUpViews()
    }

    private fun setUpViews() {
        setUpDriverAvatar()
        binding.layoutTime.hide()
        binding.layoutPaymentMethod.hide()
        binding.layoutMoney.hide()

        binding.tvCarInfo.text = StringUtils.getBoldSpanString("Chevrolet Cobalt, 95 A 123 WE", "95 A 123 WE", "#2C2D2E")
        binding.btnCall.setOnClickListener {
            sendNumberToDial("+99312345678")
        }
        binding.btnCancel.setOnClickListener {
            showCancelWaitingBottomSheet()
        }
    }

    private fun sendNumberToDial(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = android.net.Uri.parse("tel:$phone")
        startActivity(intent)
    }

    private fun showCancelWaitingBottomSheet() {
        CancelTripFragment().show(childFragmentManager, CancelTripFragment.TAG)
    }

    private fun setUpDriverAvatar() {
        Glide.with(this)
            .load("https://randomuser.me/api/portraits/women/3.jpg")
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivDriver)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rideBottomSheetViewModel.rideState.collect{ rideBottomSheetUiState  ->
                    when(rideBottomSheetUiState){
                        RideBottomSheetUiState.Error -> {}
                        RideBottomSheetUiState.Loading -> {}
                        is RideBottomSheetUiState.Success -> when(rideBottomSheetUiState.rideState){
                            RideState.WAITING_FOR_DRIVER ->  setUpWaitingForDriver()
                            RideState.DRIVER_IS_WAITING -> setUpDriverIsWaiting()
                            RideState.IN_RIDE -> setUpInRide()
                            RideState.FINISHED ->   bottomSheetNavigation.goToRideFinishedBottomSheetFromRideBottomSheet()
                            RideState.DRIVER_CANCELED -> showDriverCanceledBottomSheet()
                        }
                    }
                }
            }
        }
    }

    private fun setUpWaitingForDriver() {
        binding.tvTitle.text = "Aydawshı ~4 minut ishinde jetip keledi"

        binding.layoutFromLocation.show()
        binding.layoutToLocation.show()

        binding.layoutTime.hide()
        binding.layoutPaymentMethod.hide()
        binding.layoutMoney.hide()

        binding.ivDriver.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setUpDriverIsWaiting() {
        binding.tvTitle.text = "Aydawshı sizdi kútip tur"

        binding.layoutTime.show()
        waitingCountDownTimer.start()

        binding.layoutMoney.hide()
        binding.layoutPaymentMethod.hide()
        binding.layoutFromLocation.hide()
        binding.layoutToLocation.hide()

        binding.layoutTime.setOnClickListener {
            showWaitingTimeBottomSheet()
        }
    }
    private fun setUpInRide() {
        binding.tvTitle.text = "Mánzilge jetip barıw waqtı ~13 minut"
        waitingCountDownTimer.cancel()

        binding.layoutMoney.show()
        binding.layoutPaymentMethod.show()
        binding.layoutToLocation.show()
        binding.layoutToLocationIcon.show()
        binding.layoutToLocationIcon.setBackgroundResource(com.aralhub.ui.R.drawable.ic_material_symbols_radio_button_checked)
        binding.layoutToLocationIcon.setBackgroundColor(resources.getColor(com.aralhub.ui.R.color.color_interactive_accent))

        binding.layoutFromLocation.hide()
        binding.layoutTime.hide()
    }


    private fun showDriverCanceledBottomSheet() {
        TripCanceledByDriverFragment().show(childFragmentManager, TripCanceledByDriverFragment.TAG)
    }
    private fun showWaitingTimeBottomSheet() {
        WaitingTimeFragment().show(childFragmentManager, WaitingTimeFragment.TAG)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        waitingCountDownTimer.cancel()
    }
    companion object {
        const val TAG = "WaitingForDriverFragment"
    }
}