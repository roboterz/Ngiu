package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.RecordDetail


class AccountPRDetailViewModel : ViewModel() {

    var listPRDetail: List<RecordDetail> = ArrayList()
    var accountBalance: Double = 0.00
    var lendAmount: Double = 0.00
    var receiveAmount: Double = 0.00
    var borrowAmount: Double = 0.00
    var payAmount: Double = 0.00
    var accountID: Long = 0L
    var accountName: String =""
    var accountTypeID: Long = 0L

    fun loadDataToRam(context: Context){
        listPRDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(accountID)

        lendAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(accountID, 8L)
        receiveAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(accountID, 10L)
        borrowAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(accountID, 7L)
        payAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(accountID, 9L)

        accountBalance = lendAmount + payAmount - receiveAmount - borrowAmount
    }
}