package com.aralhub.ui.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberFormatter(
    private val editText: EditText,
    private val isFinished: (Boolean) -> Unit = {}
) {
    private var isFormatting = false
    private val prefix = "+998 "
    private val maxLength = 9 // Only the digits after +998 (9 digits for 94 148 05 15)

    init {
        // Set initial text to prefix
        editText.setText(prefix)
        editText.setSelection(prefix.length)

        editText.addTextChangedListener(object : TextWatcher {
            private var previousText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousText = s?.toString() ?: ""
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val currentText = s?.toString() ?: ""

                // Handle backspace - if text is shorter than prefix, restore prefix
                if (currentText.length < prefix.length) {
                    editText.setText(prefix)
                    editText.setSelection(prefix.length)
                    isFormatting = false
                    return
                }

                // Ensure prefix is intact
                if (!currentText.startsWith(prefix)) {
                    editText.setText(prefix)
                    editText.setSelection(prefix.length)
                    isFormatting = false
                    return
                }

                // Get only the digits after prefix
                val digitsAfterPrefix = currentText.substring(prefix.length).replace(" ", "")
                    .filter { it.isDigit() }
                    .take(maxLength)

                val formatted = prefix + formatRemainingDigits(digitsAfterPrefix)

                editText.setText(formatted)
                editText.setSelection(formatted.length)

                // Check if the required number of digits is entered
                isFinished(digitsAfterPrefix.length == maxLength)


                isFormatting = false
            }
        })

        // Prevent cursor from moving before prefix
        editText.setOnClickListener {
            if (editText.selectionStart < prefix.length) {
                editText.setSelection(prefix.length)
            }
        }
    }

    private fun formatRemainingDigits(digits: String): String {
        if (digits.isEmpty()) return ""

        val builder = StringBuilder()

        // Area code (998)
        builder.append(digits.substring(0, minOf(2, digits.length)))

        // First part of subscriber number (148)
        if (digits.length > 2) {
            builder.append(" ")
            builder.append(digits.substring(2, minOf(5, digits.length)))
        }

        // Second part (05)
        if (digits.length > 5) {
            builder.append(" ")
            builder.append(digits.substring(5, minOf(7, digits.length)))
        }

        // Last part (15)
        if (digits.length > 7) {
            builder.append(" ")
            builder.append(digits.substring(7, digits.length))
        }

        return builder.toString()
    }
}