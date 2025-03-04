package com.aralhub.ui.adapter.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.databinding.ItemLocationDraggableBinding
import com.aralhub.ui.model.LocationItem
import com.aralhub.ui.model.LocationItemDiffCallback

class LocationItemDraggableAdapter :
    ListAdapter<LocationItem, LocationItemDraggableAdapter.ViewHolder>(LocationItemDiffCallback) {

    inner class ViewHolder(private val binding: ItemLocationDraggableBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationItem) {
            binding.name.text = item.title
            binding.iconRemove.setOnClickListener {
                val currentList = currentList.toMutableList()
                currentList.removeAt(adapterPosition)
                submitList(currentList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocationDraggableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}