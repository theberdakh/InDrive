package com.aralhub.indrive.driver.orders

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.indrive.driver.orders.adapter.OrderItemAdapter
import com.aralhub.indrive.driver.orders.databinding.FragmentOrdersBinding
import com.aralhub.indrive.driver.orders.model.OrderItem
import com.aralhub.indrive.driver.orders.sheet.ExitLineModalBottomSheet
import com.aralhub.indrive.driver.orders.sheet.FilterModalBottomSheet
import com.aralhub.indrive.driver.orders.sheet.OrderModalBottomSheet
import com.aralhub.ui.utils.viewBinding

class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    private val adapter = OrderItemAdapter()
    private val orders = listOf(
        OrderItem(
            1,
            "Jo lohn Doe",
            "Pick up location",
            "https://randomuser.me/api/portraits/men/1.jpg",
            "1000 som",
            "5 km",
            "10 km"
        ),
        OrderItem(
            2,
            "Jane Doe",
            "Pick upcation",
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
        binding.tbOrders.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.rvOrders.adapter = adapter
        adapter.submitList(orders)
        adapter.setOnItemClickListener {
            OrderModalBottomSheet().show(childFragmentManager, OrderModalBottomSheet.TAG)
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
        binding.btnFilter.setOnClickListener {
            FilterModalBottomSheet().show(childFragmentManager, FilterModalBottomSheet.TAG)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            showExitLineBottomSheet()
        }
    }

    private fun showExitLineBottomSheet() {
        ExitLineModalBottomSheet(onExit = { findNavController().navigateUp() }).show(
            childFragmentManager,
            ExitLineModalBottomSheet.TAG
        )
    }
}