package com.aralhub.araltaxi.saved_places

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aralhub.araltaxi.saved_places.databinding.FragmentSavedPlacesBinding
import com.aralhub.ui.utils.viewBinding

class SavedPlacesFragment: Fragment(R.layout.fragment_saved_places) {
    private val binding by viewBinding(FragmentSavedPlacesBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbSavedPlaces.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}