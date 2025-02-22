package com.aralhub.araltaxi.profile.driver.sheet

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.aralhub.araltaxi.profile.driver.R
import com.aralhub.araltaxi.profile.driver.databinding.ModalBottomSheetTelegramBotBinding
import com.aralhub.ui.utils.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TelegramBotModalBottomSheet :
    BottomSheetDialogFragment(R.layout.modal_bottom_sheet_telegram_bot) {
    private val binding by viewBinding(ModalBottomSheetTelegramBotBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToTelegram.setOnClickListener {
            openBot(username = "aralhub")
        }
        binding.btnBack.setOnClickListener {
            dismissAllowingStateLoss()
        }

    }

    private fun isAppAvailable(context: Context, appName: String?): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: NameNotFoundException) {
            return false
        }
    }

    private fun openBot(username: String) {
        val intent = if (isAppAvailable(requireContext(), "org.telegram.messenger")) {
            Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$username"))
        } else {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$username"))
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "TelegramBotModalBottomSheet"
        fun show(fragmentManager: FragmentManager) {
            TelegramBotModalBottomSheet().show(fragmentManager, TAG)
        }
    }
}