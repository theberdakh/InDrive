package com.aralhub.araltaxi.savedplaces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.araltaxi.saved_places.databinding.ItemAddressBinding

class AddressItemAdapter: ListAdapter<AddressItem, AddressItemAdapter.AddressItemViewHolder>(AddressItemDiffCallback) {

    inner class AddressItemViewHolder(private val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(addressItem: AddressItem){
            binding.tvTitle.text = addressItem.name
            binding.tvSubtitle.text = addressItem.address
            when(addressItem.category){
                AddressCategory.HOME -> binding.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
                AddressCategory.WORK -> binding.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
                AddressCategory.OTHER -> binding.ivIcon.setImageResource(com.aralhub.ui.R.drawable.ic_ic_round_pin_drop)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressItemViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}