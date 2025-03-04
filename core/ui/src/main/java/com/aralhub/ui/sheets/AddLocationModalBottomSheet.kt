package com.aralhub.ui.sheets

import android.os.Bundle
import android.view.View
import com.aralhub.ui.R
import com.aralhub.ui.adapter.location.LocationItemDraggableAdapter
import com.aralhub.ui.databinding.ModalBottomSheetAddLocationBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddLocationModalBottomSheet : BottomSheetDialogFragment(R.layout.modal_bottom_sheet_add_location) {
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
        initObservers()
        binding.rvAddresses.adapter = adapter
       // val itemTouchHelper = ItemTouchHelper(LocationItemTouchCallback())
        //itemTouchHelper.attachToRecyclerView(binding.rvAddresses)
        binding.btnAddLocation.setOnClickListener { ChooseLocationModalBottomSheet().show(parentFragmentManager,
            ChooseLocationModalBottomSheet.TAG
        ) }
    }

    private fun initObservers() {

    }

    companion object {
        const val TAG = "AddLocationModalBottomSheet"
    }
}