package com.aralhub.indrive.driver.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.indrive.driver.orders.databinding.OrderItemBinding
import com.aralhub.indrive.driver.orders.model.OrderItem
import com.aralhub.indrive.driver.orders.model.OrderItemDiffCallback

class OrderItemAdapter: ListAdapter<OrderItem, OrderItemAdapter.ViewHolder>(OrderItemDiffCallback) {
    inner class ViewHolder(private val binding: OrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(orderItem: OrderItem) {
            binding.tvDriverName.text = orderItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}