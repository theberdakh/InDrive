package com.aralhub.offers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.offers.databinding.FragmentChangePriceBinding
import com.aralhub.ui.utils.MoneyFormatter
import com.aralhub.ui.utils.viewBinding

class ChangePriceFragment: Fragment(R.layout.fragment_change_price) {
    private val binding by viewBinding(FragmentChangePriceBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MoneyFormatter(binding.etPrice)
    }
}