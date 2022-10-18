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
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {

    private var recordId: Long = 0
    var accountList: List<Account> = ArrayList()


    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(context: Context){
        accountList = AppDatabase.getDatabase(context).account().getRecordByType(2L)
    }

}