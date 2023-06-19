package com.example.ngiu.ui.report

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.example.ngiu.functions.chart.CategoryAmount


class ReportViewModel: ViewModel(){

    private var categorydata: MutableList<CategoryAmount> = ArrayList()
    private var totalAmount: Double = 0.00
    private var transType: Long = TRANSACTION_TYPE_INCOME
    private var currentMonthStart: String = ""
    private var currentMonthEnd: String = ""

    fun loadData(context: Context, startDate: String, endDate: String){

        categorydata.clear()
        categorydata = AppDatabase.getDatabase(context).trans().
                        getCategoryAmountByTransactionType(
                            transType,
                            startDate,
                            endDate
                        )

        totalAmount = 0.0
        for (i in categorydata){
            totalAmount += i.Amount
        }

        // current month (yyyy-MM-dd)
        currentMonthStart = startDate
        currentMonthEnd = endDate
    }

    fun setTransactionType(transTypeID: Long){
        transType = transTypeID
    }

    fun getCategoryAmount(): MutableList<CategoryAmount>{
        return categorydata
    }

    fun getTotalAmount(): Double{
        return totalAmount
    }

    fun getTransactionType(): Long{
        return transType
    }

    fun getCurrentMonthStart(): String{
        return currentMonthStart
    }
    fun getCurrentMonthEnd(): String{
        return currentMonthEnd
    }
}