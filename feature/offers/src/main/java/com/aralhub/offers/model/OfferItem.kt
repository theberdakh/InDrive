package com.aralhub.offers.model

import androidx.recyclerview.widget.DiffUtil

data class OfferItem(
    val id: Int,
    val driver: OfferItemDriver,
    val offeredPrice: String
)

data class OfferItemDriver(
    val name: String,
    val carName: String
)

object OfferItemDiffCallback: DiffUtil.ItemCallback<OfferItem>(){
    override fun areItemsTheSame(oldItem: OfferItem, newItem: OfferItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OfferItem, newItem: OfferItem): Boolean {
        return oldItem == newItem
    }

}