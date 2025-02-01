package com.aralhub.indrive.request

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.indrive.request.databinding.FragmentSelectLocationBinding
import com.aralhub.ui.utils.viewBinding

class SelectLocationFragment: Fragment(R.layout.fragment_select_location) {
    private val binding by viewBinding(FragmentSelectLocationBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemSelectLocation.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
        binding.itemSelectLocation.tvTitle.text = "Aral Hub"
        binding.itemSelectLocation.tvSubtitle.text = "Aral Hub, 123 Main St"
    }
}