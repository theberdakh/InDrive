package com.aralhub.indrive.request.modal

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.aralhub.indrive.request.R
import com.aralhub.indrive.request.adapter.DraggableItemLocationAdapter
import com.aralhub.indrive.request.adapter.LocationItemTouchCallback
import com.aralhub.indrive.request.databinding.ModalBottomSheetBinding
import com.aralhub.indrive.request.model.LocationItem
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddLocationModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet) {
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
    private val adapter by lazy { DraggableItemLocationAdapter() }

    private val binding by viewBinding(ModalBottomSheetBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAddresses.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(LocationItemTouchCallback(locationItems))
        itemTouchHelper.attachToRecyclerView(binding.rvAddresses)
        adapter.submitList(locationItems)
        binding.btnAddLocation.setOnClickListener {
            ChooseLocationModalBottomSheet().show(parentFragmentManager, ChooseLocationModalBottomSheet.TAG)
        }

    }

    companion object {
        const val TAG = "AddLocationModalBottomSheet"
    }
}