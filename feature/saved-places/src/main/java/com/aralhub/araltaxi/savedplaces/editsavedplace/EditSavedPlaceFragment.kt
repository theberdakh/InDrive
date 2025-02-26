package com.aralhub.araltaxi.savedplaces.editsavedplace

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aralhub.araltaxi.saved_places.R
import com.aralhub.araltaxi.saved_places.databinding.FragmentEditSavedPlaceBinding
import com.aralhub.ui.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSavedPlaceFragment: Fragment(R.layout.fragment_edit_saved_place) {
    private val binding by viewBinding(FragmentEditSavedPlaceBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val ARG_SAVED_PLACE_ID = "savedPlaceId"
        fun args(savedPlaceId: Int): Bundle {
            return Bundle().apply {
                putInt(ARG_SAVED_PLACE_ID, savedPlaceId)
            }
        }
    }
}