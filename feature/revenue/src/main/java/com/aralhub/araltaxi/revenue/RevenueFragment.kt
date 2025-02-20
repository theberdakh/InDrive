package com.aralhub.araltaxi.revenue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.revenue.databinding.FragmentRevenueBinding
import com.aralhub.ui.utils.viewBinding

class RevenueFragment: Fragment(R.layout.fragment_revenue) {
    private val binding by viewBinding(FragmentRevenueBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbRevenue.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}