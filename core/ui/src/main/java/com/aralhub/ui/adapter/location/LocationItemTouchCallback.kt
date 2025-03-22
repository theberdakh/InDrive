package com.aralhub.ui.adapter.location

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.model.LocationItem
import java.util.Collections

class LocationItemTouchCallback(private val locations: List<LocationItem>) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.ACTION_STATE_SWIPE
    ) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition
        Collections.swap(locations, fromPos, toPos)
        recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}