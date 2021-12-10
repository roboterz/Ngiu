package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.RecordDetail


class AccountGeneralDetailViewModel : ViewModel() {

    var listDetail: List<RecordDetail> = ArrayList()
    var accountBalance: Double = 0.00
    var inflowAmount: Double = 0.00
    var outflowAmount: Double = 0.00
    var accountID: Long = 0L

    fun loadDataToRam(context: Context, acctID: Long){
        accountID = acctID

        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(acctID)

        (AppDatabase.getDatabase(context).account().getAccountInitialBalance(acctID) +
            AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(acctID) +
            AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(acctID) -
            AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(acctID) -
            AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(acctID)).also { accountBalance = it }
        //lendAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 8L)
        //receiveAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 10L)
        //borrowAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 7L)
        //payAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYSubCategoryID(acctID, 9L)


    }
}