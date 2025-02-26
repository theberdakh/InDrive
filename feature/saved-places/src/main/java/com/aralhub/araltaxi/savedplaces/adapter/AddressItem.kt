package com.aralhub.araltaxi.savedplaces.adapter

import androidx.recyclerview.widget.DiffUtil

enum class AddressCategory {
    HOME,
    WORK,
    OTHER
}

data class AddressItem(
    val id: Int,
    val name: String,
    val address: String,
    val category: AddressCategory,
    val latitude: Number,
    val longitude: Number
)

object AddressItemDiffCallback: DiffUtil.ItemCallback<AddressItem>() {
    override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem == newItem
    }
}


