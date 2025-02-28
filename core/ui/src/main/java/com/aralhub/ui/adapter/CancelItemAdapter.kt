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

    inner class ViewHolder(private val binding: ItemCancelBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cancelItem: CancelItem) {
            binding.root.text = cancelItem.title
            binding.root.setOnCheckedChangeListener { _, b ->
                binding.root.text = cancelItem.title
                if(b) {
                    currentList.forEach {
                        if (it != cancelItem) {
                            it.isSelected = false
                        }
                    }
                    cancelItem.isSelected = b
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCancelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}