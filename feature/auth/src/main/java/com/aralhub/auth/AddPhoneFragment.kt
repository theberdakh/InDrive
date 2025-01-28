package com.aralhub.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.auth.databinding.FragmentAddPhoneBinding
import com.aralhub.ui.utils.viewBinding

class AddPhoneFragment : Fragment(R.layout.fragment_add_phone) {
    private val binding by viewBinding(FragmentAddPhoneBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}