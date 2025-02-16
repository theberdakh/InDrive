package com.aralhub.araltaxi.driver.orders

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.driver.orders.adapter.OrderItemAdapter
import com.aralhub.araltaxi.driver.orders.model.OrderItem
import com.aralhub.araltaxi.driver.orders.sheet.CancelTripModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ExitLineModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.FilterModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.GoingToPickUpModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.OrderModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.ReasonCancelModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideFinishedModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.RideModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.TripCanceledModalBottomSheet
import com.aralhub.araltaxi.driver.orders.sheet.WaitingForClientModalBottomSheet
import com.aralhub.indrive.driver.orders.R
import com.aralhub.indrive.driver.orders.databinding.FragmentOrdersBinding
import com.aralhub.ui.utils.viewBinding

class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private val adapter = OrderItemAdapter()
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
    private val orders = listOf(
        OrderItem(
            1,
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/1.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            2,
            "Jane Doe",
            "Pick location",
            "https://randomuser.me/api/portraits/men/2.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            3,
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/3.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            4,
            "Jane Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/4.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            5,
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/5.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            6,
            "Jane Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/6.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            7,
            "John Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/7.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initOrderModalBottomSheet()
        initGoingToPickUpModalBottomSheet()
        initWaitingForClientModalBottomSheet()
        initRideModalBottomSheet()
        initCancelTripModalBottomSheet()
        initTripCanceledModalBottomSheet()
        initReasonCancelModalBottomSheet()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) { showExitLineBottomSheet() }
    }

    private fun initReasonCancelModalBottomSheet() {
        reasonCancelModalBottomSheet.setSendReasonListener {
            reasonCancelModalBottomSheet.dismissAllowingStateLoss()
            tripCanceledModalBottomSheet.show(childFragmentManager, TripCanceledModalBottomSheet.TAG)
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
            reasonCancelModalBottomSheet.show(childFragmentManager, ReasonCancelModalBottomSheet.TAG)
        }
        cancelTripModalBottomSheet.setOnBackListener {
            cancelTripModalBottomSheet.dismissAllowingStateLoss()
        }
    }

    private fun initRideModalBottomSheet() {
        rideModalBottomSheet.setOnRideFinishedListener {
            rideModalBottomSheet.dismissAllowingStateLoss()
            rideFinishedModalBottomSheet.show(childFragmentManager, RideFinishedModalBottomSheet.TAG)
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
            waitingForClientModalBottomSheet.show(childFragmentManager, WaitingForClientModalBottomSheet.TAG)
        }
        goingToPickUpModalBottomSheet.setOnRideCanceledListener {
            cancelTripModalBottomSheet.show(childFragmentManager, CancelTripModalBottomSheet.TAG)
        }
    }

    private fun initViews() {
        initToolbar()
        initRecyclerView()
        binding.btnFilter.setOnClickListener { filterModalBottomSheet.show(childFragmentManager, FilterModalBottomSheet.TAG) }
    }

    private fun initRecyclerView() {
        binding.rvOrders.adapter = adapter
        adapter.submitList(orders)
        adapter.setOnItemClickListener { orderModalBottomSheet.show(childFragmentManager, OrderModalBottomSheet.TAG) }
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
            goingToPickUpModalBottomSheet.show(childFragmentManager, GoingToPickUpModalBottomSheet.TAG)
        }
    }

    private fun showExitLineBottomSheet() {
        exitLineModalBottomSheet.show(childFragmentManager, ExitLineModalBottomSheet.TAG)
    }
}