package com.aralhub.indrive.request

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.indrive.request.databinding.FragmentRequestTaxiBinding
import com.aralhub.ui.utils.viewBinding

class RequestTaxiFragment: Fragment(R.layout.fragment_request_taxi) {
    private val binding by viewBinding(FragmentRequestTaxiBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  binding.btnRequestTaxi.setOnClickListener {
            binding.btnRequestTaxi.startFillAnimation()
        }*/
    }
}