package com.aralhub.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.aralhub.auth.databinding.FragmentAddNameBinding
import com.aralhub.ui.utils.viewBinding

class AddNameFragment : Fragment(R.layout.fragment_add_name) {
    private val binding by viewBinding(FragmentAddNameBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.addTextChangedListener {
            binding.btnSave.isEnabled = it.toString().isNotEmpty()
        }

    }
}