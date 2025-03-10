package com.aralhub.ui.model

import androidx.recyclerview.widget.DiffUtil

data class OfferItem(
    val id: String,
    val driver: OfferItemDriver,
    val offeredPrice: String,
    val timeToArrive: String,
)

data class OfferItemDriver(
    val id: Int,
    val name: String,
    val carName: String,
    val rating: Float,
    val avatar: String
)

object OfferItemDiffCallback: DiffUtil.ItemCallback<OfferItem>(){
    override fun areItemsTheSame(oldItem: OfferItem, newItem: OfferItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OfferItem, newItem: OfferItem): Boolean {
        return oldItem == newItem
    }

}