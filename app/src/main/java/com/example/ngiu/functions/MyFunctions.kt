package com.example.ngiu.functions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.math.abs


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


fun getDayOfMonthSuffix(n: Int): String {
    if (n in 11..13) {
        return "th"
    }
    when (n % 10) {
        1 -> return "st"
        2 -> return "nd"
        3 -> return "rd"
        else -> return "th"
    }
}




// popupWindow call back function
interface SelectItem {
    fun clicked(idx: Int)
}

fun popupWindow(context: Context, titleText: String, arrayItem: Array<String>, selectItem: SelectItem){

    // Initialize a new instance of alert dialog builder object
    val builder = AlertDialog.Builder(context)

    // set Title Style
    val titleView = View.inflate(context, R.layout.popup_title, null)
    // set Title Text
    titleView.tv_popup_title_text.text = titleText

    builder.setCustomTitle(titleView)

    // Set items form alert dialog
    builder.setItems(arrayItem) { _, which ->
        // Get the dialog selected item
        selectItem.clicked(which)
    }.create().show()

    // Create a new AlertDialog using builder object
    // Finally, display the alert dialog
    //builder.create().show()

}


fun toStatementDate(day: Int): String{
    val date = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    if(calendar.get(Calendar.DAY_OF_MONTH)>day)
        calendar.add(Calendar.MONTH, 1)
    return "${calendar.get(Calendar.MONTH)+1}-$day"
}

fun toDayLeft(day: Int): String{
    val date = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

//    val month = calendar.get(Calendar.MONTH)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    if(today>day){
        calendar.add(Calendar.MONTH, 1)
    }

    if(calendar.get(Calendar.DAY_OF_YEAR)<dayOfYear){
        val cal = Calendar.getInstance()
        cal.time = date
        val days = (31 - cal.get(Calendar.DAY_OF_MONTH)) + calendar.get(Calendar.DAY_OF_YEAR)
        return "$days"
    }

    return "${calendar.get(Calendar.DAY_OF_YEAR) - dayOfYear}"
}


fun changeColor(textView: TextView, amount: Double){
    val context = textView.context
    val color  = if(amount<0) R.color.app_expense_amount else R.color.app_income_amount
    textView.setTextColor(ContextCompat.getColor(context, color))
    textView.text = "$"+"%.2f".format(amount)

}




 fun calculateAmount(balance: Double, tran: Trans): Double{
    return when(tran.TransactionType_ID){
        in arrayOf<Long>(1,3,4)-> balance - tran.Transaction_Amount
        2L -> balance + tran.Transaction_Amount
        else -> balance
    }
}

fun getInternationalDateFromAmericanDate(string: String): LocalDateTime {
    val lDateTime: LocalDateTime =  LocalDateTime.parse(string, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
    return LocalDateTime.parse(lDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}




