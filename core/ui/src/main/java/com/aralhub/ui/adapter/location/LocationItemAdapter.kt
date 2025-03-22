package com.aralhub.ui.adapter.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.databinding.ItemLocationBinding
import com.aralhub.ui.model.LocationItem
import com.aralhub.ui.model.LocationItemDiffCallback

class LocationItemAdapter: ListAdapter<LocationItem, LocationItemAdapter.ViewHolder>(
    LocationItemDiffCallback
) {

    private var onItemClickListener: ((LocationItem) -> Unit) = {}
    fun setOnItemClickListener(listener: (LocationItem) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(locationItem: LocationItem) {
            binding.apply {
                tvTitle.text = locationItem.title
                tvSubtitle.text = locationItem.subtitle
                ivIcon.setImageResource(locationItem.icon)
                root.setOnClickListener { onItemClickListener(locationItem)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(getItem(position))

}