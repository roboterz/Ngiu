package com.aerolite.ngiu.functions

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.inflate
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.entities.Trans
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
//import kotlinx.android.synthetic.main.popup_title.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


class MyFunctions {

}




/**--- Hide the Soft Keyboard ---**/
fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

//    fun Fragment.hideKeyboard() {
//        view?.let { activity?.hideKeyboard(it) }
//    }
//
//    fun Activity.hideKeyboard() {
//        hideKeyboard(currentFocus ?: View(this))
//    }
//
//    fun Context.hideKeyboard(view: View) {
//        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//    }



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
    return if (n in 11..13) {
        "th"
    }else {
        when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
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
    titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = titleText

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

fun popupMsgWindow(context: Context, titleText: String, msgText: String): Int{

    // Initialize a new instance of alert dialog builder object
    val builder = AlertDialog.Builder(context)

    // set Title Style
    val titleView = View.inflate(context, R.layout.popup_msg_dialog, null)
    // set Title Text
    titleView.findViewById<TextView>(R.id.tv_popup_msg_title).text = titleText

    //builder.setCustomTitle(titleView)
    builder.setView(titleView)
        .setMessage(msgText)
        .setCancelable(true)
        .setPositiveButton("Confirm") { _,_ ->
            return@setPositiveButton
        }
        .setNegativeButton("Cancel") { _,_ ->
            return@setNegativeButton
        }

    // Set items form alert dialog
    builder.create().show()

    // Create a new AlertDialog using builder object
    // Finally, display the alert dialog
    //builder.create().show()
    return 0
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
    // fix bug for double type
    val tempAmount = (amount * 100).roundToInt().toDouble() / 100

    val context = textView.context
    val color  = if(tempAmount < 0) R.color.app_expense_amount else R.color.app_income_amount
    textView.setTextColor(ContextCompat.getColor(context, color))
    textView.text = ""+"%.2f".format(tempAmount)

}


fun invalidForm(context: Context, nameInputLayout: TextInputLayout) {

    var message = ""
    if (nameInputLayout.helperText != null) {
        message += ContextCompat.getString(context, R.string.msg_account_name) + ": " + nameInputLayout.helperText
    }
//    else if (balanceInputLayout.helperText != null) {
//        message += ContextCompat.getString(context, R.string.msg_account_name) + ": " + balanceInputLayout.helperText
//    }

    // set Title Style
    val titleView = inflate(context, R.layout.popup_title,null)
    // set Title Text
    titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = ContextCompat.getString(context, R.string.msg_Title_invalid_form)

    val dialog =  AlertDialog.Builder(context)
        .setCustomTitle(titleView)
        .setMessage(message)
        .setPositiveButton(ContextCompat.getString(context, R.string.msg_button_ok)) { _, _ ->
            // do nothing
        }
        .create()

    dialog.show()

    // set button color
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.app_button_text_highlight))
}


 fun calculateAmount(balance: Double, tran: Trans): Double{
    return when(tran.TransactionType_ID){
        in arrayOf<Long>(1,3,4)-> balance - tran.Transaction_Amount
        2L -> balance + tran.Transaction_Amount
        else -> balance
    }
}

// return international date format
fun getInternationalDateFromAmericanDate(string: String): LocalDateTime {
    val lDateTime: LocalDateTime =  LocalDateTime.parse(string, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
    return LocalDateTime.parse(lDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}


fun get2DigitFormat(double: Double): String{
    return "$%.2f".format((double * 100).roundToInt().toDouble() / 100)
}


fun switchToAccountAttributePage(view: View, acctTypeID: Long,
                                 keyValue_acctID: Long, keyValue_acctBalance: Double,
                                 mode: Int){
    //*** switch to Account Attribute page (create a new account | edit a account)

        // Pass Data
        // Add Mode | Edit Mode
        val bundle = Bundle().apply {
            // KEY_VALUE_ACCOUNT_ADD_CASH = Account_TYPE_CASH (Add Mode, by analogy)
            // KEY_VALUE_ACCOUNT_ADD_CASH - Account_TYPE_CASH = 20 (Edit Mode,by analogy)
            putLong(
                KEY_ACCOUNT_PAGE, acctTypeID +
                    if (mode == NEW_MODE) 0 else 20)
            putLong(KEY_ACCOUNT_ID, keyValue_acctID)
            putDouble(KEY_ACCOUNT_BALANCE, keyValue_acctBalance)
        }

        when (acctTypeID) {
            ACCOUNT_TYPE_CASH -> {
                view.findNavController().navigate(R.id.addCashFragment, bundle)
            }
            ACCOUNT_TYPE_CREDIT -> {
                view.findNavController().navigate(R.id.addCreditFragment, bundle)
            }
            ACCOUNT_TYPE_DEBIT -> {
                view.findNavController().navigate(R.id.addDebitFragment, bundle)
            }
            ACCOUNT_TYPE_INVESTMENT -> {
                view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
            }
            ACCOUNT_TYPE_WEB -> {
                view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
            }
            ACCOUNT_TYPE_STORED -> {
                view.findNavController().navigate(R.id.addFixedAssetsFragment, bundle)
            }
            ACCOUNT_TYPE_VIRTUAL -> {
                view.findNavController().navigate(R.id.addWebAccountFragment, bundle)
            }
            ACCOUNT_TYPE_ASSETS -> {
                view.findNavController().navigate(R.id.addFixedAssetsFragment, bundle)
            }
            ACCOUNT_TYPE_RECEIVABLE -> {
                view.findNavController().navigate(R.id.addCashFragment, bundle)
            }
        }

}

fun switchToCategoryManager(view: View, fragment: Fragment, mode: Int, transTypeID: Long) {

    // Put Data Before switch
    fragment.setFragmentResult(
        KEY_CATEGORY_MANAGER, bundleOf(
        KEY_CATEGORY_MANAGER_MODE to mode,
        KEY_CATEGORY_MANAGER_TRANSACTION_TYPE to transTypeID
    ))

    // switch to category manage fragment
    view.findNavController().navigate(R.id.navigation_category_manage)
}


fun switchToAccountListFragment(view: View, fragment: Fragment, mode: Int, transTypeID: Long, cateID: Long, exceptAcctID: Long) {

    // Put Data Before switch
    fragment.setFragmentResult(
        KEY_ACCOUNT_LIST, bundleOf(
        KEY_ACCOUNT_LIST_MODE to mode,
        KEY_ACCOUNT_LIST_TRANSACTION_TYPE_ID to transTypeID,
        KEY_ACCOUNT_LIST_CATEGORY_ID to cateID,
        KEY_ACCOUNT_LIST_EXCEPT_ID to exceptAcctID
    ))

    // switch to category manage fragment
    view.findNavController().navigate(R.id.navigation_account_list)
}


fun switchToRecordFragment(
    view: View, fragment: Fragment, transID: Long = 0,
    acctID: Long = 0, transTypeID: Long = TRANSACTION_TYPE_EXPENSE
){
/*    val bundle = Bundle().apply {
        putLong(KEY_RECORD_TRANSACTION_ID, transID)
    }*/

    // put data before switch
    fragment.setFragmentResult(
        KEY_RECORD, bundleOf(
        KEY_RECORD_TRANSACTION_ID to transID,
        KEY_RECORD_ACCOUNT_ID to acctID,
        KEY_RECORD_TRANSACTION_TYPE_ID to transTypeID
    ))
    // switch to record fragment
    view.findNavController().navigate(R.id.navigation_record)
}

