package com.example.ngiu.ui.calendar

import android.content.Context
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Person
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {

    private var recordId: Long = 0
    var accountList: MutableList<Account> = ArrayList()


    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(context: Context){
        val today:Int =  LocalDateTime.now().dayOfMonth
        val aList = AppDatabase.getDatabase(context).account().getRecordByType(2L)

        accountList.clear()
        //for (i in aList.indices){
        //    if (aList[i].Account_PaymentDay >= today){
                accountList.addAll(aList.filter { it.Account_PaymentDay>=today })
                accountList.addAll(aList.filter { it.Account_PaymentDay < today })
        //    }
        //}

    }

    fun updateAccountFixedPayment(context: Context, accountID:Long, blnFixed: Boolean){

        val acct = AppDatabase.getDatabase(context).account().getRecordByID(accountID)

        acct.Account_FixedPaymentDay = blnFixed

        AppDatabase.getDatabase(context).account().updateAccount(acct)

    }
}