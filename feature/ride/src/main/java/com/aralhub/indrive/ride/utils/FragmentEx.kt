package com.aralhub.indrive.ride.utils

import android.content.Intent
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object FragmentEx {

    fun Fragment.sendPhoneNumberToDial(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = android.net.Uri.parse("tel:$phone")
        startActivity(intent)
    }

    fun Fragment.loadAvatar(url: String, imageView: ImageView) {
        Glide.with(this)
            .load("https://randomuser.me/api/portraits/women/3.jpg")
            .centerCrop()
            .placeholder(com.aralhub.ui.R.drawable.ic_user)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }
}