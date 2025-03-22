package com.aralhub.ui.adapter.option

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.databinding.ItemRideOptionBinding
import com.aralhub.ui.model.RideOptionItem
import com.aralhub.ui.model.RideOptionItemDiffCallback

class RideOptionItemAdapter: ListAdapter<RideOptionItem, RideOptionItemAdapter.ViewHolder>(
    RideOptionItemDiffCallback
) {

    inner class ViewHolder(private val binding: ItemRideOptionBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rideOption: RideOptionItem){
            binding.tvTitle.text = rideOption.title
            binding.toggleSwitch.isChecked()
            binding.toggleSwitch.setOnCheckedListener {
                rideOption.isEnabled = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRideOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}