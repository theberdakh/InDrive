package com.aralhub.ui.model

import androidx.recyclerview.widget.DiffUtil

data class CancelItem(
    val id: Int,
    val title: String,
    var isSelected: Boolean = false
)

object CancelItemDiffCallback: DiffUtil.ItemCallback<CancelItem>(){
    override fun areItemsTheSame(oldItem: CancelItem, newItem: CancelItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CancelItem, newItem: CancelItem): Boolean {
        return oldItem == newItem
    }
}
