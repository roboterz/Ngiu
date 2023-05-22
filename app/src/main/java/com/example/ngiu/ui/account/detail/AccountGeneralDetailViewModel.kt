package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.returntype.RecordDetail


class AccountGeneralDetailViewModel : ViewModel() {

    var listDetail: List<RecordDetail> = ArrayList()
    var accountBalance: Double = 0.00
    var inflowAmount: Double = 0.00
    var outflowAmount: Double = 0.00
    var accountID: Long = 0L
    var accountName: String =""
    var accountTypeID: Long = 0L

    fun loadDataToRam(context: Context){

        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(accountID)

        val initialBalance = AppDatabase.getDatabase(context).account().getAccountInitialBalance(accountID)
        val totalAmountOfIncome = AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(accountID)
        val totalAmountOfExpense = AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(accountID)
        val totalAmountOfTransferOut = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(accountID)
        val totalAmountOfTransferIn = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(accountID)

        //accountBalance = initialBalance + totalAmountOfIncome + totalAmountOfTransferIn - totalAmountOfExpense - totalAmountOfTransferOut

        accountBalance = initialBalance + totalAmountOfExpense + totalAmountOfTransferOut - totalAmountOfIncome - totalAmountOfTransferIn

        inflowAmount = totalAmountOfIncome + totalAmountOfTransferIn
        outflowAmount = totalAmountOfExpense + totalAmountOfTransferOut

    }
}