package com.aralhub.ui.model.profile

import androidx.recyclerview.widget.DiffUtil

enum class ProfileItemCategory { NAME, PHONE, CAR, CAR_COLOR, CAR_NUMBER, CARD }
data class ProfileItem(
    val title: String,
    val subtitle: String,
    val type: ProfileItemCategory
)

data class DriverInfoUI(
    val fullName: String,
    val phoneNumber: String,
    val avatar: String
)

object ProfileItemDiffCallback : DiffUtil.ItemCallback<ProfileItem>() {
    override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
        return oldItem == newItem
    }
}