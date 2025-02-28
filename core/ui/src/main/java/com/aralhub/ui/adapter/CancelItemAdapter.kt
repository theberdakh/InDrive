package com.aralhub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.databinding.ItemCancelBinding
import com.aralhub.ui.model.CancelItem
import com.aralhub.ui.model.CancelItemDiffCallback

class CancelItemAdapter: ListAdapter<CancelItem, CancelItemAdapter.ViewHolder>(
    CancelItemDiffCallback
) {
    private var selectedPosition = RecyclerView.NO_POSITION

    private var onItemSelected: ((CancelItem) -> Unit) = {}

    inner class ViewHolder(private val binding: ItemCancelBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cancelItem: CancelItem, position: Int) {
            binding.root.text = cancelItem.title
            binding.root.isChecked = position == selectedPosition
            cancelItem.isSelected = position == selectedPosition
            binding.root.setOnClickListener(null)
            binding.root.setOnClickListener {
                if (position != selectedPosition) {
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    if (previousPosition != RecyclerView.NO_POSITION) {
                        getItem(previousPosition).isSelected = false
                        notifyItemChanged(previousPosition)
                    }
                    getItem(position).isSelected = true
                    notifyItemChanged(position)
                    onItemSelected.invoke(getItem(position))
                }
                else {
                    getItem(position).isSelected = false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCancelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}