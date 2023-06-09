package com.example.salesapp.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import java.text.NumberFormat
import java.util.*

fun EditText.addCurrencyFormatter() {
    this.addTextChangedListener(object : TextWatcher {

        private var current = ""

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s.toString() != current) {
                this@addCurrencyFormatter.removeTextChangedListener(this)

                val cleanString = s.toString().replace("\\D".toRegex(), "")
                val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()

                val valueDouble = parsed / 100
                val formatted = valueDouble.formatToBrazilianCurrency()
                current = formatted
                this@addCurrencyFormatter.setText(formatted)
                this@addCurrencyFormatter.setSelection(formatted.length)
                this@addCurrencyFormatter.addTextChangedListener(this)
            }
        }
    })
}

fun Double.formatToBrazilianCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
}

fun String.removeFormatter(): String {
    val cleanString = this.replace("\\D".toRegex(), "")
    val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()

    val formatted = NumberFormat.getInstance(Locale("pt", "BR")).format(parsed / 100)
    val newFormat = formatted.replace(".", "")
    return newFormat.replace(",", ".")
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

