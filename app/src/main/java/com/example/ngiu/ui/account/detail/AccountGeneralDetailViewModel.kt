package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.example.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.example.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.example.ngiu.functions.TRANSACTION_TYPE_TRANSFER


class AccountGeneralDetailViewModel : ViewModel() {

    var listDetail: List<RecordDetail> = ArrayList()
    var listBalance: MutableList<Double> = ArrayList()
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

        accountBalance = initialBalance - totalAmountOfExpense - totalAmountOfTransferOut + totalAmountOfIncome + totalAmountOfTransferIn

        inflowAmount = totalAmountOfIncome + totalAmountOfTransferIn
        outflowAmount = totalAmountOfExpense + totalAmountOfTransferOut


        listBalance = Array(listDetail.size) { 0.00 }.toMutableList()

        var totalBalance = initialBalance

        for (i in listDetail.indices.reversed()){
            when (listDetail[i].TransactionType_ID){
                TRANSACTION_TYPE_EXPENSE -> {
                    totalBalance -= listDetail[i].Transaction_Amount
                }
                TRANSACTION_TYPE_INCOME -> {
                    totalBalance += listDetail[i].Transaction_Amount
                }
                TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT -> {
                    if (accountID == listDetail[i].Account_ID){
                        totalBalance -= listDetail[i].Transaction_Amount
                    } else {
                        totalBalance += listDetail[i].Transaction_Amount
                    }

                }
            }

            listBalance[i] = totalBalance
        }

    }
}