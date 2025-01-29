package com.aralhub.ui.utils

import android.content.Context
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText

object EditTextUtils {

    fun EditText.hideOnDone(context: Context) {
        setOnEditorActionListener { tv, actionId, keyEvent ->
            KeyboardUtils.hideKeyboardFragment(context = context, tv)
            true

        }
    }
}