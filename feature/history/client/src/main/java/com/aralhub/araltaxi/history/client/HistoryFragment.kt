package com.aralhub.araltaxi.history.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.history.client.databinding.FragmentHistoryBinding
import com.aralhub.ui.utils.viewBinding

class HistoryFragment: Fragment(R.layout.fragment_history) {
    private val binding by viewBinding(FragmentHistoryBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbHistory.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}