package com.aerolite.ngiu.ui.report

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.returntype.TransactionDetail


class ReportDetailViewModel: ViewModel(){

//    var listDetail: List<RecordDetail> = ArrayList()
//    var accountBalance: Double = 0.00
//    var inflowAmount: Double = 0.00
//    var outflowAmount: Double = 0.00
//    var accountID: Long = 0L
//    var accountName: String =""
//    var accountTypeID: Long = 0L
//
//    fun loadDataToRam(context: Context){
//
//        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByCategory(accountID)
//
//
//
//    }

    fun getTransByCateID(context: Context, cateID: Long, startDate: String, endDate: String): List<TransactionDetail>{
        return AppDatabase.getDatabase(context).trans().getTransRecordDetailByCategory(cateID, startDate, endDate)
    }

    fun getCategoryNameByID(context: Context, cateID: Long): String{
        return AppDatabase.getDatabase(context).category().getCategoryByID(cateID).Category_Name
    }

    fun getReportTypeByCategoryID(context: Context, cateID: Long): Long{
        return AppDatabase.getDatabase(context).category().getCategoryByID(cateID).TransactionType_ID
    }
}