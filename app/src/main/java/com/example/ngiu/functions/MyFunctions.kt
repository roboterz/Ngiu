package com.example.ngiu.functions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText


class MyFunctions {

    @SuppressLint("RestrictedApi")
    fun switchToFragment(view: View, resID: Int, withBackStack: Boolean){
        // pop out fragment from back stack
        if (!withBackStack) view.findNavController().popBackStack()
        // switch to fragment
        view.findNavController().navigate(resID)
    }


}


// limit decimal
fun EditText.addDecimalLimiter(maxLimit: Int = 2) {

    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            val str = this@addDecimalLimiter.text!!.toString()
            if (str.isEmpty()) return
            val str2 = decimalLimiter(str, maxLimit)

            if (str2 != str) {
                this@addDecimalLimiter.setText(str2)
                val pos = this@addDecimalLimiter.text!!.length
                this@addDecimalLimiter.setSelection(pos)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    })
}


fun EditText.decimalLimiter(string: String, MAX_DECIMAL: Int): String {

    var str = string
    if (str[0] == '.') str = "0$str"
    val max = str.length

    var rFinal = ""
    var after = false
    var i = 0
    var up = 0
    var decimal = 0
    var t: Char

    val decimalCount = str.count{ ".".contains(it) }

    if (decimalCount > 1)
        return str.dropLast(1)

    while (i < max) {
        t = str[i]
        if (t != '.' && !after) {
            up++
        } else if (t == '.') {
            after = true
        } else {
            decimal++
            if (decimal > MAX_DECIMAL)
                return rFinal
        }
        rFinal += t
        i++
    }
    return rFinal
}



fun TextInputEditText.addDecimalLimiter(maxLimit: Int = 2) {

    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            val str = this@addDecimalLimiter.text!!.toString()
            if (str.isEmpty()) return
            val str2 = decimalLimiter(str, maxLimit)

            if (str2 != str) {
                this@addDecimalLimiter.setText(str2)
                val pos = this@addDecimalLimiter.text!!.length
                this@addDecimalLimiter.setSelection(pos)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    })
}

fun TextInputEditText.decimalLimiter(string: String, MAX_DECIMAL: Int): String {

    var str = string
    if (str[0] == '.') str = "0$str"
    val max = str.length

    var rFinal = ""
    var after = false
    var i = 0
    var up = 0
    var decimal = 0
    var t: Char

    val decimalCount = str.count{ ".".contains(it) }

    if (decimalCount > 1)
        return str.dropLast(1)

    while (i < max) {
        t = str[i]
        if (t != '.' && !after) {
            up++
        } else if (t == '.') {
            after = true
        } else {
            decimal++
            if (decimal > MAX_DECIMAL)
                return rFinal
        }
        rFinal += t
        i++
    }
    return rFinal
}
