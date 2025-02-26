package com.aralhub.araltaxi.history.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.core.common.error.ErrorHandler
import com.aralhub.araltaxi.history.client.databinding.FragmentHistoryBinding
import com.aralhub.indrive.core.data.model.address.CreateAddressRequest
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel by viewModels<HistoryViewModel>()
    @Inject lateinit var errorHandler: ErrorHandler
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
   }

    private fun initListeners() {
        binding.tbHistory.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun initObservers() {
    }
}