package com.example.ngiu.ui.activity

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ActivityViewModel : ViewModel() {


    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    */

    var transactionDetail: List<TransactionDetail> = ArrayList()

    var monthExpense: Double = 0.00
    var monthIncome: Double = 0.00
    var budget: Double = 0.00

    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(context: Context){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val lDate = LocalDate.now().withDayOfMonth(1)
        val fromDate = lDate.format(formatter)
        val toDate = lDate.plusMonths(1).format(formatter)

        monthExpense = AppDatabase.getDatabase(context).trans().getMonthExpense(fromDate, toDate)
        //budget =
        monthIncome = AppDatabase.getDatabase(context).trans().getMonthIncome(fromDate, toDate)
        transactionDetail = AppDatabase.getDatabase(context).trans().getAllTrans()
    }



}