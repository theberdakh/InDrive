package com.aralhub.indrive.request.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.indrive.request.databinding.LocationItemBinding
import com.aralhub.indrive.request.model.LocationItem
import com.aralhub.indrive.request.model.LocationItemDiffCallback

class LocationItemAdapter: ListAdapter<LocationItem, LocationItemAdapter.ViewHolder>(
    LocationItemDiffCallback
) {
    inner class ViewHolder(private val binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(locationItem: LocationItem) {
            binding.apply {
                tvTitle.text = locationItem.title
                tvSubtitle.text = locationItem.subtitle
                ivIcon.setImageResource(locationItem.icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(getItem(position))

}