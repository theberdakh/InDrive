package com.aralhub.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import com.aralhub.ui.R

class LoadingDialog(context: Context) {
    private val dialog: Dialog = Dialog(context).apply {
        setContentView(R.layout.dialog_fragment_loading)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private val handler = Handler(Looper.getMainLooper())

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
            handler.postDelayed({ dialog.setCancelable(true) }, 2000)
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
            handler.removeCallbacksAndMessages(null)
        }
    }
}