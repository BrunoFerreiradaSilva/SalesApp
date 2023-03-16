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

                val formated =
                    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(parsed / 100)

                current = formated
                this@addCurrencyFormatter.setText(formated)
                this@addCurrencyFormatter.setSelection(formated.length)
                this@addCurrencyFormatter.addTextChangedListener(this)
            }
        }
    })
}

fun String.removeFormatter(): String {
    val cleanString = this.replace("\\D".toRegex(), "")
    val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()

    val formated = NumberFormat.getInstance().format(parsed / 100)
    val newFormat = formated.replace(".","")
    return newFormat.replace(",",".")
}

fun View.gone(){
    this.visibility = View.GONE
}
