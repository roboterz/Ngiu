package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans


class AccountPRDetailViewModel : ViewModel() {

    var listPRDetail: List<Trans> = ArrayList()
    var accountBalance: Double = 0.00
    var lendAmount: Double = 0.00
    var receiveAmount: Double = 0.00
    var borrowAmount: Double = 0.00
    var payAmount: Double = 0.00

    fun loadDataToRam(context: Context, acctID: Long){
        listPRDetail = AppDatabase.getDatabase(context).trans().getRecordsByAccountAndAccountRecipientID(acctID)
        (AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(3L) +
                AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(3L) -
                AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(3L) -
                AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(3L)).also { accountBalance = it }
        lendAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 8L)
        receiveAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 10L)
        borrowAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 7L)
        payAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 9L)
    }
}