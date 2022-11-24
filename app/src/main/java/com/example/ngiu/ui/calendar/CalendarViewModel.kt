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
import com.example.ngiu.data.entities.returntype.CalendarDetail
import com.example.ngiu.data.entities.returntype.TransactionDetail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {

    private var recordId: Long = 0
    //var accountList: MutableList<Account> = ArrayList()

    var calendarDetail: MutableList<CalendarDetail> = ArrayList()


    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(context: Context){
        val today:Int =  LocalDate.now().dayOfMonth
        val acctList = AppDatabase.getDatabase(context).account().getRecordByType(2L)
        val eventList = AppDatabase.getDatabase(context).event().getAllEvent()

        calendarDetail.clear()

        //load account list
        for (i in acctList.indices) {
            val cd = CalendarDetail()
            cd.apply {
                this.account_id = acctList[i].Account_ID
                this.title = acctList[i].Account_Name
                this.type = 1
                this.account_last_four_number = acctList[i].Account_CardNumber
                if (today <= acctList[i].Account_PaymentDay) {
                    this.date =
                        LocalDate.now().plusDays((acctList[i].Account_PaymentDay - today).toLong())
                } else {
                    this.date = LocalDate.now().plusMonths(1)
                        .minusDays((today - acctList[i].Account_PaymentDay).toLong())
                }
            }
            calendarDetail.add(cd)
        }

        //load event list
        for (i in eventList.indices){
            val cd = CalendarDetail()
            cd.apply {
                this.type = 3
                this.date = eventList[i].Event_Date.toLocalDate()
                this.memo = eventList[i].Event_Memo
            }
            calendarDetail.add(cd)
        }


        // sort by date
        calendarDetail.sortBy { it.date }

    }



/*    fun updateAccountFixedPayment(context: Context, accountID:Long, blnFixed: Boolean){

        val acct = AppDatabase.getDatabase(context).account().getRecordByID(accountID)

        acct.Account_FixedPaymentDay = blnFixed

        AppDatabase.getDatabase(context).account().updateAccount(acct)

    }*/
}