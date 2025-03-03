package com.aralhub.araltaxi.driver.orders.orders

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.aralhub.ui.utils.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private val adapter = OrderItemAdapter()
    @Inject lateinit var errorHandler: ErrorHandler
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
    private val exitLineModalBottomSheet = ExitLineModalBottomSheet { findNavController().navigateUp() }
    private val orders = mutableListOf(
        OrderItem(
            "1",
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/1.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "2",
            "Jane Doe",
            "Pick location",
            "https://randomuser.me/api/portraits/men/2.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "3",
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/3.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "4",
            "Jane Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/4.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "5",
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/5.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "6",
            "Jane Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/6.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            "7",
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/7.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
    )
    @Inject lateinit var navigation: FeatureOrdersNavigation

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
        observeState(viewModel.profileUiState){ profileUiState ->
            when (profileUiState) {
                is ProfileUiState.Error -> errorHandler.showToast(profileUiState.message)
                ProfileUiState.Loading -> {}
                is ProfileUiState.Success -> displayProfile(profileUiState.driverProfile)
            }
        }

        observeState(viewModel.logoutUiState){ logoutUiState ->
                when (logoutUiState) {
                    is LogoutUiState.Error -> errorHandler.showToast(logoutUiState.message)
                    LogoutUiState.Loading -> {}
                    LogoutUiState.Success -> navigation.goToLogoFromOrders()
                }
        }

        observeState(viewModel.ordersState) { getActiveOrdersUiState ->
            when (getActiveOrdersUiState) {
                is GetActiveOrdersUiState.Error -> errorHandler.showToast(getActiveOrdersUiState.message)
                GetActiveOrdersUiState.Loading -> {
                    // show loading
                    viewModel.sendLocation(
                        SendDriverLocationUI(
                            latitude = 42.44668,
                            longitude = 59.618043,
                            distance = 3000
                        )
                    )
                }
                is GetActiveOrdersUiState.OrderCanceled -> {
                    orders.removeIf { it.id == getActiveOrdersUiState.rideId }
                    adapter.submitList(orders)
                }
                is GetActiveOrdersUiState.GetNewOrder -> {
                    orders.add(getActiveOrdersUiState.data)
                    adapter.submitList(orders)
                }
                is GetActiveOrdersUiState.GetExistOrder -> {}
            }
        }

        observeState(viewModel.existingOrdersState){ getActiveOrdersUiState ->
            when (getActiveOrdersUiState) {
                is GetActiveOrdersUiState.Error -> errorHandler.showToast(getActiveOrdersUiState.message)
                GetActiveOrdersUiState.Loading -> {}
                is GetActiveOrdersUiState.OrderCanceled -> {
                    orders.removeIf { it.id == getActiveOrdersUiState.rideId }
                    adapter.submitList(orders)
                }
                is GetActiveOrdersUiState.GetNewOrder -> {}
                is GetActiveOrdersUiState.GetExistOrder -> {
                    adapter.submitList(getActiveOrdersUiState.data.toMutableList())
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) { showExitLineBottomSheet() }
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
            cancelTripModalBottomSheet.dismissAllowingStateLoss()
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
        adapter.submitList(orders)
        adapter.setOnItemClickListener {
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