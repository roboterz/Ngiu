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

class CalendarViewModel : ViewModel() {

    var calendarDetail: MutableList<CalendarDetail> = ArrayList()


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

        var eventTitle: String = context.getString(R.string.event_credit_card_payment)

        //load account list
        for (i in acctList.indices) {
            val cd = CalendarDetail()
            cd.apply {
                this.id = acctList[i].Account_ID
                this.account_out_name = acctList[i].Account_Name
                this.type = EVENT_CREDIT_PAYMENT
                this.title = eventTitle
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

        eventTitle = context.getString(R.string.event_reminder)
        //load event list
        for (i in eventList.indices) {
            val cd = CalendarDetail()
            cd.apply {
                this.id = eventList[i].Event_ID
                this.title = eventTitle
                this.type = EVENT_NOTE
                this.date = eventList[i].Event_Date.toLocalDate()
                this.memo = eventList[i].Event_Memo
            }
            calendarDetail.add(cd)
        }


        // sort by date
        calendarDetail.sortBy { it.date }

    }


}
