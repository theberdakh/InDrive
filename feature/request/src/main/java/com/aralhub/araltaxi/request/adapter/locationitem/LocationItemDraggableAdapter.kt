package com.aralhub.araltaxi.request.adapter.locationitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.araltaxi.client.request.databinding.LocationItemDraggableBinding
import com.aralhub.araltaxi.request.adapter.locationitem.model.LocationItem
import com.aralhub.araltaxi.request.adapter.locationitem.model.LocationItemDiffCallback

class LocationItemDraggableAdapter :
    ListAdapter<LocationItem, LocationItemDraggableAdapter.ViewHolder>(LocationItemDiffCallback) {

    inner class ViewHolder(private val binding: LocationItemDraggableBinding) :
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
            LocationItemDraggableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}