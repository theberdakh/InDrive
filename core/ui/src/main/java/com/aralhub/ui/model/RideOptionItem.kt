package com.aralhub.ui.model

import androidx.recyclerview.widget.DiffUtil

data class RideOptionItem(
    val id: Int,
    val title: String,
    var isEnabled: Boolean
)

object RideOptionItemDiffCallback: DiffUtil.ItemCallback<RideOptionItem>(){
    override fun areItemsTheSame(oldItem: RideOptionItem, newItem: RideOptionItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RideOptionItem, newItem: RideOptionItem): Boolean {
        return oldItem == newItem
    }

}
