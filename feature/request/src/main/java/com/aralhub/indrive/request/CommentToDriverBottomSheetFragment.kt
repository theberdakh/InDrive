package com.aralhub.indrive.request

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentToDriverBottomSheetFragment: BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_comment_to_driver) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
      //  initDialog(dialog)
        return dialog
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initDialog(dialog: BottomSheetDialog) {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}
