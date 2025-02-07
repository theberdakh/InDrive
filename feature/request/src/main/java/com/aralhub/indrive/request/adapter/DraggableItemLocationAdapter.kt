package com.aralhub.indrive.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.indrive.request.databinding.DraggableItemLocationBinding
import com.aralhub.indrive.request.model.LocationItem
import com.aralhub.indrive.request.model.LocationItemDiffCallback

class DraggableItemLocationAdapter :
    ListAdapter<LocationItem, DraggableItemLocationAdapter.ViewHolder>(LocationItemDiffCallback) {

    inner class ViewHolder(private val binding: DraggableItemLocationBinding) :
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
            DraggableItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}