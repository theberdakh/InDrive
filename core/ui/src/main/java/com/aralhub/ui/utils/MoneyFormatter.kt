package com.aralhub.ui.utils

import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.aralhub.ui.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MoneyFormatter(
    private val editText: EditText,
    private val isFinished: (Boolean) -> Unit = {}
) {
    private var isFormatting = false
    private val decimalFormat: DecimalFormat = DecimalFormat("#,###").apply {
        decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = ' '
        }
    }

    private val suffix = " som"

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digitsOnly = s?.toString()?.replace(" ", "")?.filter { it.isDigit() } ?: ""

                if (digitsOnly.isEmpty()) {
                    editText.setText("")
                    isFormatting = false
                    return
                }

                val formattedText = formatNumber(digitsOnly)
                editText.setText(formatWithSuffix(formattedText))
                editText.setSelection(formattedText.length)

                isFinished(digitsOnly.isNotEmpty())
                isFormatting = false
            }
        })

        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val currentText = editText.text.toString().replace(suffix, "").trim()
            editText.setText(formatWithSuffix(currentText))
            editText.setSelection(currentText.length)
        }
    }

    private fun formatNumber(digits: String): String {
        return decimalFormat.format(digits.toLong())
    }

    private fun formatWithSuffix(text: String): SpannableString {
        Log.d("MoneyFormatter", "formatWithSuffix: $text")
        val result = SpannableString("$text$suffix")
        result.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    editText.context,
                    R.color.color_content_tertiary
                )
            ),
            text.length, result.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return result
    }

    fun getUnformattedText(): String {
        return editText.text.toString().replace(suffix, "").replace(" ", "").trim()
    }

    fun unformattedText(editText: EditText): String {
        return editText.text.toString().replace(suffix, "").replace(" ", "").trim()
    }
}