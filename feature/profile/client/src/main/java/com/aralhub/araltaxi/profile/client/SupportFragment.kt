package com.aralhub.araltaxi.profile.client

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.profile.client.databinding.FragmentSupportBinding
import com.aralhub.araltaxi.profile.client.sheet.TelegramBotModalBottomSheet
import com.aralhub.ui.utils.viewBinding

class SupportFragment: Fragment(R.layout.fragment_support) {
    private val binding by viewBinding(FragmentSupportBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbSupport.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.llTelegramBot.setOnClickListener { TelegramBotModalBottomSheet.show(childFragmentManager) }
        binding.llCallCenter.setOnClickListener {
            sendNumberToDial("+998 90 300 01 01")
        }
    }

    private fun sendNumberToDial(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = android.net.Uri.parse("tel:$phone")
        startActivity(intent)
    }



}