package com.aralhub.ui.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

object StringUtils {

    fun getBoldSpanString(fullText: String, boldText: String, boldTextColorHex: String): SpannableString {
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(boldText)
        val endIndex = startIndex + boldText.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(boldTextColorHex)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}