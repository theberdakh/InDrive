package com.aralhub.araltaxi.request.sheet.modal

import android.os.Bundle
import android.view.View
import com.aralhub.araltaxi.client.request.R
import com.aralhub.araltaxi.client.request.databinding.ModalBottomSheetChooseLocationBinding
import com.aralhub.araltaxi.request.adapter.LocationItemAdapter
import com.aralhub.araltaxi.request.model.LocationItem
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseLocationModalBottomSheet: BottomSheetDialogFragment(R.layout.modal_bottom_sheet_choose_location) {
    private val binding by viewBinding(ModalBottomSheetChooseLocationBinding::bind)
    private val locationItemAdapter by lazy { LocationItemAdapter() }
    private val locationItems = listOf(
        LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        ), LocationItem(
            id = 1,
            title = "Home",
            subtitle = "123 Main St"
        ),
        LocationItem(
            id = 2,
            title = "Work",
            subtitle = "456 Business St"
        )
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAddresses.adapter = locationItemAdapter
        locationItemAdapter.submitList(locationItems)
    }

    companion object{
        const val TAG = "ChooseLocationModalBottomSheet"
    }
}