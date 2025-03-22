package com.aralhub.ui.model

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil

enum class LocationItemClickOwner {
    FROM, TO
}

data class LocationItem(
    val id: Int,
    val title: String,
    @DrawableRes val icon: Int = com.aralhub.ui.R.drawable.ic_ic_round_pin_drop,
    val subtitle: String,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val clickOwner: LocationItemClickOwner
)

object LocationItemDiffCallback : DiffUtil.ItemCallback<LocationItem>() {
    override fun areItemsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
        return oldItem == newItem
    }
}