package com.aralhub.indrive.ride

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aralhub.indrive.ride.modal.CancelTripFragment
import com.aralhub.indrive.ride.modal.TripCanceledByDriverFragment
import com.aralhub.indrive.ride.modal.WaitingTimeFragment
import com.aralhub.indrive.ride.navigation.FeatureWaitingNavigation
import com.aralhub.indrive.waiting.R
import com.aralhub.indrive.waiting.databinding.FragmentSheetRideBinding
import com.aralhub.ui.utils.StringUtils
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideSheetFragment : Fragment(R.layout.fragment_sheet_ride) {
    private val waitingCountDownTimer = WaitingCountDownTimer(
        onCountDownTimerTick = ::onCountDownTimerTick,
        onCountDownTimerFinish = ::onCountDownTimerFinish
    )
    private fun onCountDownTimerFinish() {}
    private fun onCountDownTimerTick(minutes: Long, seconds: Long) {
        binding.tvTime.text = "$minutes:$seconds"
    }
    @Inject
    lateinit var waitingNavigation: FeatureWaitingNavigation

    private val binding by viewBinding(FragmentSheetRideBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setUpViews()
    }

    private fun setUpViews() {
        setUpWaitingForDriver()
        setUpDriverAvatar()
        setUpCountDown()
        binding.tvCarInfo.text = StringUtils.getBoldSpanString("Chevrolet Cobalt, 95 A 123 WE", "95 A 123 WE", "#2C2D2E")
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = android.net.Uri.parse("tel:+99312345678")
            startActivity(intent)
        }
        binding.btnCancel.setOnClickListener {
            showCancelWaitingBottomSheet()
        }
    }

    private fun showCancelWaitingBottomSheet() {
        CancelTripFragment().show(childFragmentManager, CancelTripFragment.TAG)
    }

    private fun setUpCountDown() {
        waitingCountDownTimer.start()
    }

    private fun setUpWaitingForDriver() {
        binding.layoutTime.isVisible = false
        binding.layoutRoute.isVisible = true
        binding.tvTitle.text = "Aydawshı ~4 minut ishinde jetip keledi"
        binding.ivDriver.setOnClickListener {
            findNavController().navigateUp()
        }
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
                delay(5000)
                setUpDriverIsWaiting()
                delay(1000)
                showDriverCanceledBottomSheet()
            }
        }
    }

    private fun showDriverCanceledBottomSheet() {
        TripCanceledByDriverFragment().show(childFragmentManager, TripCanceledByDriverFragment.TAG)
    }

    private fun setUpDriverIsWaiting() {
        binding.layoutTime.isVisible = true
        binding.layoutRoute.isVisible = false
        binding.tvTitle.text = "Aydawshı sizdi kútip tur"

        binding.layoutTime.setOnClickListener {
            showWaitingTimeBottomSheet()
        }
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