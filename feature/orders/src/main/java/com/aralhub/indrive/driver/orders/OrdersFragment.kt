package com.aralhub.indrive.driver.orders

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.indrive.driver.orders.databinding.FragmentOrdersBinding
import com.aralhub.indrive.driver.orders.sheet.ExitLineModalBottomSheet
import com.aralhub.indrive.driver.orders.sheet.FilterModalBottomSheet
import com.aralhub.ui.utils.viewBinding

class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val binding by viewBinding(FragmentOrdersBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.btnFilter.setOnClickListener {
            FilterModalBottomSheet().show(childFragmentManager, FilterModalBottomSheet.TAG)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            showExitLineBottomSheet()
        }
    }

    private fun showExitLineBottomSheet() {
        ExitLineModalBottomSheet(onExit = { findNavController().navigateUp() }).show(childFragmentManager, ExitLineModalBottomSheet.TAG)
    }
}