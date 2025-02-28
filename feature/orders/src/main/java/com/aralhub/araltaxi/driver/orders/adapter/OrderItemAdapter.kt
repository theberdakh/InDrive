package com.aralhub.araltaxi.driver.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.aralhub.araltaxi.driver.orders.model.OrderItem
import com.aralhub.indrive.driver.orders.databinding.OrderItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class OrderItemAdapter :
    Adapter<OrderItemAdapter.ViewHolder>() {

    private var listOfOrders = mutableListOf<OrderItem>()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    private var onItemClickListener: ((OrderItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (OrderItem) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem) {
            binding.tvClientName.text = orderItem.name
            binding.tvPickUpLocation.text = orderItem.pickUp
            binding.tvDistance.text = orderItem.pickUpDistance
            binding.tvDistanceRoad.text = orderItem.roadDistance
            binding.tvPrice.text = orderItem.roadPrice

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(orderItem)
            }

            Glide.with(binding.ivAvatar.context)
                .load(orderItem.avatar)
                .centerCrop()
                .placeholder(com.aralhub.ui.R.drawable.ic_user)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listOfOrders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(listOfOrders[position])

    fun submitList(list: MutableList<OrderItem>) {
        this.listOfOrders = list
    }

    object OrderItemDiffCallback : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }

    }
}