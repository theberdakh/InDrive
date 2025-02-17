package com.aralhub.araltaxi.profile.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.client.databinding.FragmentSupportBinding
import com.aralhub.ui.utils.viewBinding

class SupportFragment: Fragment(R.layout.fragment_support) {
    private val binding by viewBinding(FragmentSupportBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbSupport.setNavigationOnClickListener { findNavController().navigateUp() }
    }

}