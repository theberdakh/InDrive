package com.aralhub.araltaxi.driver.orders.orders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.driver.orders.R
import com.aralhub.araltaxi.driver.orders.databinding.FragmentOrdersBinding
import com.aralhub.araltaxi.driver.orders.model.RideStatus
import com.aralhub.araltaxi.driver.orders.model.SendDriverLocationUI
import com.aralhub.araltaxi.driver.orders.navigation.FeatureOrdersNavigation
import com.aralhub.araltaxi.driver.orders.sheet.CancelTripModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ExitLineModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.FilterModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.GoingToPickUpModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.LogoutModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.OrderModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ReasonCancelModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideCancelledByPassengerModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideFinishedModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.TripCanceledModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.WaitingForClientModalBottomSheet
import com.aralhub.araltaxi.driver.orders.utils.SoundManager
import com.aralhub.araltaxi.services.LocationService
import com.aralhub.indrive.core.data.model.driver.DriverInfo
import com.aralhub.ui.adapter.OrderItemAdapter
import com.aralhub.ui.dialog.ErrorMessageDialog
import com.aralhub.ui.dialog.LoadingDialog
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.ViewEx.invisible
import com.aralhub.ui.utils.ViewEx.show
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private val adapter = OrderItemAdapter()

    @Inject
    lateinit var errorHandler: ErrorHandler

    private val viewModel by viewModels<OrdersViewModel>()
    private val startedRideStatusViewModel by viewModels<StartedRideStatusViewModel>()

    private val orderModalBottomSheet = OrderModalBottomSheet()
    private val goingToPickUpModalBottomSheet = GoingToPickUpModalBottomSheet()
    private val waitingForClientModalBottomSheet = WaitingForClientModalBottomSheet()
    private val rideModalBottomSheet = RideModalBottomSheet()
    private val rideFinishedModalBottomSheet = RideFinishedModalBottomSheet()
    private val cancelTripModalBottomSheet = CancelTripModalBottomSheet()
    private val reasonCancelModalBottomSheet = ReasonCancelModalBottomSheet()
    private val rideCanceledByPassengerModalBottomSheet = RideCancelledByPassengerModalBottomSheet()
    private val tripCanceledModalBottomSheet = TripCanceledModalBottomSheet()
    private val filterModalBottomSheet = FilterModalBottomSheet()
    private val exitLineModalBottomSheet =
        ExitLineModalBottomSheet { findNavController().navigateUp() }

    private var errorDialog: ErrorMessageDialog? = null
    private var loadingDialog: LoadingDialog? = null
    private var isResponseReceived = false

    private var soundManager: SoundManager? = null

    @Inject
    lateinit var navigation: FeatureOrdersNavigation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        errorDialog = ErrorMessageDialog(context)
        loadingDialog = LoadingDialog(context)
        soundManager = SoundManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startService()
        fetchData()
        initViews()
        initObservers()
        initGoingToPickUpModalBottomSheet()
        initWaitingForClientModalBottomSheet()
        initRideModalBottomSheet()
        initTripCanceledModalBottomSheet()
        initListeners()

    }

    private fun fetchData() {

        arguments?.let {
            showActiveRideSheet()
        }

        viewModel.getExistingOrders(
            SendDriverLocationUI(
                latitude = 42.41268,
                longitude = 59.688043,
                distance = 500000
            )
        )
        viewModel.getDriverProfile()
    }

    private fun showActiveRideSheet() {
        goingToPickUpModalBottomSheet.arguments = arguments
        goingToPickUpModalBottomSheet.show(
            childFragmentManager,
            GoingToPickUpModalBottomSheet.TAG
        )
    }

    private fun startService() {
        val intent = Intent(requireContext(), LocationService::class.java)
        requireActivity().startService(intent)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ordersListState.collect { orders ->
                dismissLoading()
                adapter.submitList(orders)
                binding.tvOrdersNotFound.visibility =
                    if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        observeState(viewModel.profileUiState) { profileUiState ->
            when (profileUiState) {
                is ProfileUiState.Error -> showErrorDialog(profileUiState.message)
                ProfileUiState.Loading -> showLoading()
                is ProfileUiState.Success -> displayProfile(profileUiState.driverProfile)
            }
        }

        observeState(viewModel.logoutUiState) { logoutUiState ->
            when (logoutUiState) {
                is LogoutUiState.Error -> showErrorDialog(logoutUiState.message)
                LogoutUiState.Loading -> showLoading()
                LogoutUiState.Success -> navigation.goToLogoFromOrders()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.combinedOrdersState.collectLatest { getActiveOrdersUiState ->
                    when (getActiveOrdersUiState) {
                        is GetActiveOrdersUiState.Error -> showErrorDialog(getActiveOrdersUiState.message)

                        GetActiveOrdersUiState.Loading -> showLoading()

                        is GetActiveOrdersUiState.OrderCanceled -> {
//                            adapter.submitList(orders)
                        }

                        is GetActiveOrdersUiState.OfferRejected -> {

                        }

                        is GetActiveOrdersUiState.OfferAccepted -> {
                            val bundle = Bundle()
                            bundle.putParcelable(
                                "OrderDetail",
                                getActiveOrdersUiState.data
                            )
                            goingToPickUpModalBottomSheet.arguments = bundle
                            goingToPickUpModalBottomSheet.show(
                                childFragmentManager,
                                GoingToPickUpModalBottomSheet.TAG
                            )
                            viewModel.updateRideStatus(
                                getActiveOrdersUiState.data.id,
                                RideStatus.DRIVER_ON_THE_WAY.status
                            )
                            orderModalBottomSheet.dismissAllowingStateLoss()
                            startedRideStatus()
                        }

                        is GetActiveOrdersUiState.GetNewOrder -> {
//                            adapter.submitList(orders)
                            soundManager?.playSound()
                            binding.tvOrdersNotFound.invisible()
                        }

                        is GetActiveOrdersUiState.GetExistOrder -> {
                            dismissLoading()
                            if (getActiveOrdersUiState.data.isNotEmpty()) {
                                binding.tvOrdersNotFound.invisible()
//                                adapter.submitList(getActiveOrdersUiState.data)
                            } else {
                                binding.tvOrdersNotFound.show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startedRideStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                startedRideStatusViewModel.startedRideStatus.collectLatest { result ->
                    when (result) {
                        is GetStartedRideStatusUiState.Error -> showErrorDialog(result.message)

                        is GetStartedRideStatusUiState.RideCanceled -> {
                            rideCanceledByPassengerModalBottomSheet.show(
                                childFragmentManager,
                                rideCanceledByPassengerModalBottomSheet.tag
                            )
                        }
                    }
                }
            }
        }
    }

    private fun displayProfile(driverProfile: DriverInfo) {
        dismissLoading()
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            driverProfile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            driverProfile.phoneNumber
        Glide.with(requireContext())
            .load("https://araltaxi.aralhub.uz/${driverProfile.avatar}")
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .signature(ObjectKey(System.currentTimeMillis()))
            .apply(RequestOptions.circleCropTransform())
            .into(binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.iv_avatar))
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            true
        ) { showExitLineBottomSheet() }
        binding.navigationView.getHeaderView(0).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navigation.goToProfileFromOrders()
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_support -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToSupportFromOrders()
                    true
                }

                R.id.action_log_out -> {
                    val logoutModalBottomSheet = LogoutModalBottomSheet()
                    logoutModalBottomSheet.show(childFragmentManager, LogoutModalBottomSheet.TAG)
                    logoutModalBottomSheet.setOnLogoutListener {
                        logoutModalBottomSheet.dismissAllowingStateLoss()
                        viewModel.logout()
                    }
                    true
                }

                R.id.action_my_revenue -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToRevenueFromOrders()
                    true
                }

                R.id.action_order_history -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navigation.goToHistoryFromOrders()
                    true
                }

                else -> false
            }
        }
        orderModalBottomSheet.setOnAddressClickListener { order: OrderItem? ->
            navigation.goToMapFromOrders(
                order ?: return@setOnAddressClickListener
            )
        }

        rideCanceledByPassengerModalBottomSheet.setOnUnderstandClickListener {
            dismissAllBottomSheets()
            viewModel.getExistingOrders(
                SendDriverLocationUI(
                    latitude = 42.41268,
                    longitude = 59.688043,
                    distance = 500000
                )
            )
        }
    }

    private fun initTripCanceledModalBottomSheet() {

        cancelTripModalBottomSheet.setOnRideCancelClickListener { order: OrderItem? ->
            val bundle = Bundle()
            bundle.putInt("rideId", order?.id ?: 0)
            reasonCancelModalBottomSheet.arguments = bundle
            reasonCancelModalBottomSheet.show(
                childFragmentManager,
                ReasonCancelModalBottomSheet.TAG
            )
        }

        tripCanceledModalBottomSheet.setOnCloseListener {
            tripCanceledModalBottomSheet.dismissAllowingStateLoss()
            dismissAllBottomSheets()
        }

        reasonCancelModalBottomSheet.setOnRideCancelledListener {
            Log.d("OrdersFragment", "click")
            tripCanceledModalBottomSheet.show(
                childFragmentManager,
                TripCanceledModalBottomSheet.TAG
            )
            reasonCancelModalBottomSheet.dismissAllowingStateLoss()
        }
    }

    private fun initRideModalBottomSheet() {
        rideModalBottomSheet.setOnRideFinishedListener { order: OrderItem? ->
            rideModalBottomSheet.dismissAllowingStateLoss()
            rideFinishedModalBottomSheet.show(
                childFragmentManager,
                RideFinishedModalBottomSheet.TAG
            )
            viewModel.updateRideStatus(
                order!!.id,
                RideStatus.RIDE_COMPLETED.status
            )
            viewModel.getExistingOrders(
                SendDriverLocationUI(
                    latitude = 42.41268,
                    longitude = 59.688043,
                    distance = 500000
                )
            )
        }
        rideModalBottomSheet.setOnRideCanceledListener {
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initWaitingForClientModalBottomSheet() {
        waitingForClientModalBottomSheet.setOnGoingToRideListener { order: OrderItem? ->
            waitingForClientModalBottomSheet.dismissAllowingStateLoss()
            val bundle = Bundle()
            bundle.putParcelable(
                "OrderDetail",
                order
            )
            rideModalBottomSheet.arguments = bundle
            rideModalBottomSheet.show(childFragmentManager, RideModalBottomSheet.TAG)
            viewModel.updateRideStatus(
                order?.id!!,
                RideStatus.RIDE_STARTED.status
            )
        }
        waitingForClientModalBottomSheet.setOnRideCanceledListener {
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initGoingToPickUpModalBottomSheet() {
        goingToPickUpModalBottomSheet.setOnClientPickedUp { order: OrderItem? ->
            goingToPickUpModalBottomSheet.dismissAllowingStateLoss()
            val bundle = Bundle()
            bundle.putParcelable(
                "OrderDetail",
                order
            )
            waitingForClientModalBottomSheet.arguments = bundle
            waitingForClientModalBottomSheet.show(
                childFragmentManager,
                WaitingForClientModalBottomSheet.TAG
            )
            viewModel.updateRideStatus(
                order!!.id,
                RideStatus.DRIVER_WAITING_CLIENT.status
            )
        }
        goingToPickUpModalBottomSheet.setOnRideCanceledListener { order ->
            val bundle = Bundle()
            bundle.putParcelable("OrderDetail", order)
            cancelTripModalBottomSheet.arguments = bundle
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initViews() {
        initToolbar()
        initRecyclerView()
        binding.btnFilter.setOnClickListener {
            filterModalBottomSheet.show(
                childFragmentManager,
                FilterModalBottomSheet.TAG
            )
        }
    }

    private fun initRecyclerView() {
        binding.rvOrders.adapter = adapter
        adapter.setOnItemClickListener { order ->
            val bundle = Bundle()
            bundle.putParcelable(
                "OrderDetail",
                order
            )
            orderModalBottomSheet.arguments = bundle
            orderModalBottomSheet.show(
                childFragmentManager,
                OrderModalBottomSheet.TAG
            )
        }
    }

    private fun initToolbar() {
        binding.tbOrders.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.tbOrders.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_exit_line -> {
                    showExitLineBottomSheet()
                    true
                }

                else -> false
            }
        }
    }

    private fun showExitLineBottomSheet() {
        exitLineModalBottomSheet.show(childFragmentManager, ExitLineModalBottomSheet.TAG)
    }

    private fun dismissAllBottomSheets() {
        listOf(
            orderModalBottomSheet,
            goingToPickUpModalBottomSheet,
            waitingForClientModalBottomSheet,
            rideModalBottomSheet,
            rideFinishedModalBottomSheet,
            cancelTripModalBottomSheet,
            tripCanceledModalBottomSheet,
            filterModalBottomSheet,
            exitLineModalBottomSheet
        ).forEach { sheet ->
            if (sheet.isAdded) sheet.dismissAllowingStateLoss()
        }
    }

    private fun showErrorDialog(errorMessage: String?) {
        errorDialog?.show(errorMessage)
    }

    private fun showLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!isResponseReceived) {
                loadingDialog?.show()
            }
        }
    }

    private fun dismissLoading() {
        isResponseReceived = true
        loadingDialog?.dismiss()
    }

    private fun dismissErrorDialog() {
        errorDialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissErrorDialog()
        dismissLoading()
    }

}