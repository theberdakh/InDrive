package com.aralhub.araltaxi.request.model

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil

data class LocationItem(
    val id: Int,
    val title: String,
    @DrawableRes val icon: Int = com.aralhub.ui.R.drawable.ic_carbon_location_current,
    val subtitle: String
)

object LocationItemDiffCallback : DiffUtil.ItemCallback<LocationItem>() {
    override fun areItemsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
        return oldItem == newItem
    }
}