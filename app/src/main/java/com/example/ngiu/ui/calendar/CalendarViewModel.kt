package com.example.ngiu.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.loader.ResourcesProvider
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.text.Editable
import androidx.core.content.res.TypedArrayUtils.getNamedString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Event
import com.example.ngiu.data.entities.Person
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.CalendarDetail
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class CalendarViewModel : ViewModel() {

    var calendarDetail: MutableList<CalendarDetail> = ArrayList()
    var pendingPayment = 0.00
    var pendingIncome = 0.00

    // return event record
    fun getEventRecord(context: Context, event_ID: Long): Event {
        return AppDatabase.getDatabase(context).event().getRecordByID(event_ID)
    }

    // delete event record
    fun deleteEventRecord(context: Context, event_ID: Long){
        return AppDatabase.getDatabase(context).event().deleteEvent(Event(Event_ID = event_ID))
    }

    // save event record
    fun saveEventRecord(context: Context, event: Event){
        if (event.Event_ID == 0L) {
            AppDatabase.getDatabase(context).event().addEvent(event)
        }else{
            AppDatabase.getDatabase(context).event().updateEvent(event)
        }
    }


    fun loadDataToRam(context: Context) {
        val today: Int = LocalDate.now().dayOfMonth
        val acctList = AppDatabase.getDatabase(context).account().getRecordByType(ACCOUNT_TYPE_CREDIT)
        val eventList = AppDatabase.getDatabase(context).event().getAllEvent()

        calendarDetail.clear()
        pendingPayment = 0.00
        pendingIncome = 0.00


        var eventTitle: String = context.getString(R.string.event_credit_card_payment)

        //load account list
        for (i in acctList.indices) {
            val cd = CalendarDetail()
            cd.apply {
                //
                this.id = acctList[i].Account_ID
                //
                this.account_out_name = acctList[i].Account_Name
                //
                this.type = EVENT_CREDIT_PAYMENT
                //
                this.title = eventTitle
                //
                this.account_last_four_number = acctList[i].Account_CardNumber
                // date
                if (today <= acctList[i].Account_PaymentDay) {
                    this.date =
                        LocalDateTime.now().plusDays((acctList[i].Account_PaymentDay - today).toLong())
                } else {
                    this.date = LocalDateTime.now().minusDays((today - acctList[i].Account_PaymentDay).toLong())
                        .plusMonths(1)

                }
                // amount
                this.amount = getArrears(context, acctList[i].Account_ID, acctList[i].Account_Balance)
            }

            if (cd.amount < 0.0) {
                calendarDetail.add(cd)
            }

            pendingPayment -= cd.amount
        }

        eventTitle = context.getString(R.string.event_reminder)
        //load event list
        for (i in eventList.indices) {
            val cd = CalendarDetail()
            cd.apply {
                this.id = eventList[i].Event_ID
                this.title = eventTitle
                this.type = EVENT_NOTE
                this.date = eventList[i].Event_Date
                this.memo = eventList[i].Event_Memo
            }
            calendarDetail.add(cd)
        }


        // sort by date
        calendarDetail.sortBy { it.date }

    }

    private fun getArrears(context: Context, accountID: Long, balance: Double): Double{
        val totalAmountOfIncome = AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(accountID)
        val totalAmountOfExpense = AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(accountID)
        val totalAmountOfTransferOut = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(accountID)
        val totalAmountOfTransferIn = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(accountID)

        // Arrears
        val arrears = ( balance - totalAmountOfExpense - totalAmountOfTransferOut + totalAmountOfIncome + totalAmountOfTransferIn )

        //return get2DigitFormat(arrears).toDouble()
        return (arrears * 100).roundToInt().toDouble() / 100
    }


}
