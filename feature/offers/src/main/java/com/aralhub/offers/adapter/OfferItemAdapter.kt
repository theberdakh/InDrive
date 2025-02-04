package com.aralhub.offers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.offers.databinding.ItemOfferBinding
import com.aralhub.offers.model.OfferItem
import com.aralhub.offers.model.OfferItemDiffCallback

class OfferItemAdapter: ListAdapter<OfferItem, OfferItemAdapter.ViewHolder>(OfferItemDiffCallback) {

    inner class ViewHolder(private val itemOfferBinding: ItemOfferBinding): RecyclerView.ViewHolder(itemOfferBinding.root){
        fun bind(offerItem: OfferItem){
            itemOfferBinding.tvName.text = offerItem.driver.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}