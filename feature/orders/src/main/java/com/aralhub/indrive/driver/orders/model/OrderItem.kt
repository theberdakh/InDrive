package com.aralhub.indrive.driver.orders.model

import androidx.recyclerview.widget.DiffUtil

data class OrderItem(
    val id: Int,
    val name: String,
    val pickUp: String
)

object OrderItemDiffCallback: DiffUtil.ItemCallback<OrderItem>(){
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }

}