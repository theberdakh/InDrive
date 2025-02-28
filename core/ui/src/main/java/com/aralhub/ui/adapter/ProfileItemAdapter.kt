package com.aralhub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aralhub.ui.databinding.ProfileItemBinding
import com.aralhub.ui.model.profile.ProfileItem
import com.aralhub.ui.model.profile.ProfileItemDiffCallback

class ProfileItemAdapter: ListAdapter<ProfileItem, ProfileItemAdapter.ViewHolder>(
    ProfileItemDiffCallback
) {

    inner class ViewHolder(private val binding: ProfileItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ProfileItem){
            binding.tvTitle.text = item.title
            binding.tvSubtitle.text = item.subtitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}