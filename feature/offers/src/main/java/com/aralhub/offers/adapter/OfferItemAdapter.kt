package com.aralhub.offers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.offers.databinding.ItemOfferBinding
import com.aralhub.offers.model.OfferItem
import com.aralhub.offers.model.OfferItemDiffCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class OfferItemAdapter :
    ListAdapter<OfferItem, OfferItemAdapter.ViewHolder>(OfferItemDiffCallback) {

    private var onItemAcceptClickListener: ((OfferItem) -> Unit)? = null
    fun setOnItemAcceptClickListener(listener: (OfferItem) -> Unit) {
        onItemAcceptClickListener = listener
    }

    inner class ViewHolder(private val itemOfferBinding: ItemOfferBinding) :
        RecyclerView.ViewHolder(itemOfferBinding.root) {
        fun bind(offerItem: OfferItem) {
            itemOfferBinding.btnAccept.setOnClickListener {
                onItemAcceptClickListener?.invoke(offerItem)
            }
            itemOfferBinding.apply {
                tvDriverName.text = offerItem.driver.name
                tvPrice.text = offerItem.offeredPrice + " som"
                tvRating.text = "${offerItem.driver.rating} ★"
                tvTime.text = offerItem.timeToArrive
                tvCar.text = offerItem.driver.carName

                Glide.with(itemOfferBinding.ivAvatar.context)
                    .load(offerItem.driver.avatar)
                    .centerCrop()
                    .placeholder(com.aralhub.ui.R.drawable.ic_user)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemOfferBinding.ivAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}