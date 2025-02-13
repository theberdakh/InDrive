package com.aralhub.araltaxi.request.sheet.modal

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.aralhub.araltaxi.request.adapter.LocationItemDraggableAdapter
import com.aralhub.araltaxi.request.adapter.LocationItemTouchCallback
import com.aralhub.araltaxi.request.model.LocationItem
import com.aralhub.indrive.request.R
import com.aralhub.indrive.request.databinding.ModalBottomSheetAddLocationBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddLocationModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_add_location) {
    private val locationItems = arrayListOf(
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
    private val adapter by lazy { LocationItemDraggableAdapter() }
    private val binding by viewBinding(ModalBottomSheetAddLocationBinding::bind)

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAddresses.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(LocationItemTouchCallback(locationItems))
        itemTouchHelper.attachToRecyclerView(binding.rvAddresses)
        adapter.submitList(locationItems)
        binding.btnAddLocation.setOnClickListener { ChooseLocationModalBottomSheet().show(parentFragmentManager, ChooseLocationModalBottomSheet.TAG) }
    }

    companion object {
        const val TAG = "AddLocationModalBottomSheet"
    }
}