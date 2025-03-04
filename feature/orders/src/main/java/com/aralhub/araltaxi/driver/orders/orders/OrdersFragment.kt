package com.aralhub.araltaxi.driver.orders.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.driver.orders.model.SendDriverLocationUI
import com.aralhub.araltaxi.driver.orders.navigation.FeatureOrdersNavigation
import com.aralhub.araltaxi.driver.orders.sheet.CancelTripModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ExitLineModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.FilterModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.GoingToPickUpModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.LogoutModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.OrderModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ReasonCancelModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideFinishedModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.TripCanceledModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.WaitingForClientModalBottomSheet
import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.FragmentOrdersBinding
import com.aralhub.ui.adapter.OrderItemAdapter
import com.aralhub.ui.model.OrderItem
import com.aralhub.ui.utils.LifecycleOwnerEx.observeState
import com.aralhub.ui.utils.ViewEx.invisible
import com.aralhub.ui.utils.ViewEx.show
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private val adapter = OrderItemAdapter()

    private var rideId: Int = 0

    @Inject
    lateinit var errorHandler: ErrorHandler
    private val viewModel by viewModels<OrdersViewModel>()
    private val orderModalBottomSheet = OrderModalBottomSheet()
    private val goingToPickUpModalBottomSheet = GoingToPickUpModalBottomSheet()
    private val waitingForClientModalBottomSheet = WaitingForClientModalBottomSheet()
    private val rideModalBottomSheet = RideModalBottomSheet()
    private val rideFinishedModalBottomSheet = RideFinishedModalBottomSheet()
    private val cancelTripModalBottomSheet = CancelTripModalBottomSheet()
    private val tripCanceledModalBottomSheet = TripCanceledModalBottomSheet()
    private val filterModalBottomSheet = FilterModalBottomSheet()
    private val reasonCancelModalBottomSheet = ReasonCancelModalBottomSheet()
    private val exitLineModalBottomSheet =
        ExitLineModalBottomSheet { findNavController().navigateUp() }
    private val orders = mutableListOf<OrderItem>()

    @Inject
    lateinit var navigation: FeatureOrdersNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        initViews()
        initObservers()
        initOrderModalBottomSheet()
        initGoingToPickUpModalBottomSheet()
        initWaitingForClientModalBottomSheet()
        initRideModalBottomSheet()
        initCancelTripModalBottomSheet()
        initTripCanceledModalBottomSheet()
        initReasonCancelModalBottomSheet()
        initListeners()

    }

    private fun fetchData() {
        viewModel.getExistingOrders(
            SendDriverLocationUI(
                latitude = 42.41268,
                longitude = 59.688043,
                distance = 500000
            )
        )
        viewModel.getDriverProfile()
    }

    private fun initObservers() {
        binding.tvUuid.setOnClickListener {
            viewModel.cancelRide(
                rideId = rideId,
                cancelCauseId = 2
            )
        }
        viewModel.activeOrdersUiState.onEach { rideId ->
            binding.tvUuid.text = rideId.toString()
            this.rideId = rideId
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        observeState(viewModel.profileUiState) { profileUiState ->
            when (profileUiState) {
                is ProfileUiState.Error -> errorHandler.showToast(profileUiState.message)
                ProfileUiState.Loading -> {}
                is ProfileUiState.Success -> displayProfile(profileUiState.driverProfile)
            }
        }

        observeState(viewModel.logoutUiState) { logoutUiState ->
            when (logoutUiState) {
                is LogoutUiState.Error -> errorHandler.showToast(logoutUiState.message)
                LogoutUiState.Loading -> {}
                LogoutUiState.Success -> navigation.goToLogoFromOrders()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.combinedOrdersState.collectLatest { getActiveOrdersUiState ->
                    Log.d("OrdersFragment", "$getActiveOrdersUiState")
                    when (getActiveOrdersUiState) {
                        is GetActiveOrdersUiState.Error -> errorHandler.showToast(
                            getActiveOrdersUiState.message
                        )

                        GetActiveOrdersUiState.Loading -> {}

                        is GetActiveOrdersUiState.OrderCanceled -> {
                            orders.removeIf { it.id == getActiveOrdersUiState.rideId }
                            adapter.submitList(orders)
                            binding.tvOrdersNotFound.isVisible = orders.isEmpty()
                        }

                        is GetActiveOrdersUiState.GetNewOrder -> {
                            orders.add(getActiveOrdersUiState.data)
                            adapter.submitList(orders)
                            binding.tvOrdersNotFound.invisible()
                        }

                        is GetActiveOrdersUiState.GetExistOrder -> {
                            if (getActiveOrdersUiState.data.isNotEmpty()) {
                                binding.tvOrdersNotFound.invisible()
                                adapter.submitList(getActiveOrdersUiState.data)
                            } else {
                                binding.tvOrdersNotFound.show()
                            }
                            delay(2000)
                            viewModel.sendLocation(
                                SendDriverLocationUI(
                                    latitude = 42.44668,
                                    longitude = 59.618043,
                                    distance = 300000
                                )
                            )

                        }
                    }
                }
            }
        }
    }

    private fun displayProfile(driverProfile: DriverProfile) {
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_name).text =
            driverProfile.fullName
        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.tv_phone).text =
            driverProfile.phoneNumber
        Glide.with(requireContext())
            .load("https://araltaxi.aralhub.uz/${driverProfile.photoUrl}")
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
    }

    private fun initReasonCancelModalBottomSheet() {
        reasonCancelModalBottomSheet.setSendReasonListener {
            reasonCancelModalBottomSheet.dismissAllowingStateLoss()
            tripCanceledModalBottomSheet.show(
                childFragmentManager,
                TripCanceledModalBottomSheet.TAG
            )
        }
    }

    private fun initTripCanceledModalBottomSheet() {
        tripCanceledModalBottomSheet.setOnCloseListener {
            tripCanceledModalBottomSheet.dismissAllowingStateLoss()
            val activeBottomSheet = listOf(
                rideModalBottomSheet,
                waitingForClientModalBottomSheet,
                goingToPickUpModalBottomSheet,
                orderModalBottomSheet,
                cancelTripModalBottomSheet
            ).firstOrNull { it.isAdded }
            activeBottomSheet?.dismissAllowingStateLoss()
        }
    }

    private fun initCancelTripModalBottomSheet() {
        cancelTripModalBottomSheet.setOnCancelTripListener {
//            cancelTripModalBottomSheet.dismissAllowingStateLoss()
            viewModel.cancelRide(
                rideId,
                2
            )
            reasonCancelModalBottomSheet.show(
                childFragmentManager,
                ReasonCancelModalBottomSheet.TAG
            )
        }
        cancelTripModalBottomSheet.setOnBackListener {
            cancelTripModalBottomSheet.dismissAllowingStateLoss()
        }
    }

    private fun initRideModalBottomSheet() {
        rideModalBottomSheet.setOnRideFinishedListener {
            rideModalBottomSheet.dismissAllowingStateLoss()
            rideFinishedModalBottomSheet.show(
                childFragmentManager,
                RideFinishedModalBottomSheet.TAG
            )
        }
        rideModalBottomSheet.setOnRideCanceledListener {
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initWaitingForClientModalBottomSheet() {
        waitingForClientModalBottomSheet.setOnGoingToRideListener {
            waitingForClientModalBottomSheet.dismissAllowingStateLoss()
            rideModalBottomSheet.show(childFragmentManager, RideModalBottomSheet.TAG)
        }
        waitingForClientModalBottomSheet.setOnRideCanceledListener {
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initGoingToPickUpModalBottomSheet() {
        goingToPickUpModalBottomSheet.setOnClientPickedUp {
            goingToPickUpModalBottomSheet.dismissAllowingStateLoss()
            waitingForClientModalBottomSheet.show(
                childFragmentManager,
                WaitingForClientModalBottomSheet.TAG
            )
        }
        goingToPickUpModalBottomSheet.setOnRideCanceledListener {
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

    private fun initOrderModalBottomSheet() {
        orderModalBottomSheet.setOnOrderAccepted {
            orderModalBottomSheet.dismissAllowingStateLoss()
            goingToPickUpModalBottomSheet.show(
                childFragmentManager,
                GoingToPickUpModalBottomSheet.TAG
            )
        }
    }

    private fun showExitLineBottomSheet() {
        exitLineModalBottomSheet.show(childFragmentManager, ExitLineModalBottomSheet.TAG)
    }
}