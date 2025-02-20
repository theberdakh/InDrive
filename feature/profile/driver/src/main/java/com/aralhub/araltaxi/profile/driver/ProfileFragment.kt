package com.aralhub.araltaxi.profile.driver

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.driver.databinding.FragmentProfileBinding
import com.aralhub.ui.utils.viewBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding(FragmentProfileBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.tbProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}