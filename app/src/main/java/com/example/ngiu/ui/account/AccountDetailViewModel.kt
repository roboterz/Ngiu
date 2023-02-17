package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.functions.*
import com.example.ngiu.functions.calculateAmount
import com.example.ngiu.ui.account.model.AccountTransRecordModel
import java.time.format.DateTimeFormatter

class AccountDetailViewModel : ViewModel() {

    val accountRecordsList = MutableLiveData<List<AccountTransRecordModel>>()
    private val recordTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")
    var color = 0
    fun getInflow(context: Context, id: Long): String {
        val appDatabase = AppDatabase.getDatabase(context)
        val inflowA = appDatabase.account().getInflowA(id)
        val inflowB = appDatabase.account().getInflowB(id)
        return "%.2f".format(inflowA + inflowB)
    }

    fun getOutflow(context: Context, id: Long): String {
        val appDatabase = AppDatabase.getDatabase(context)
        val outflowA = appDatabase.account().getOutflowA(id)
        val outflowB = appDatabase.account().getOutflowB(id)
        return "%.2f".format(outflowA + outflowB)

    }

    fun getTransRecords(context: Context, id: Long, balance: Double) {
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.category().getAllCategory()
        val allRecords = appDatabase.trans().getTransRecordAccount(id).asReversed()

        val accountTransRecordList = ArrayList<AccountTransRecordModel>()

        var rBalance = balance


        allRecords
            .forEach { item ->
                val cate = allTypes.find { it.Category_ID == item.Category_ID }



                val model = AccountTransRecordModel(
                    cate?.Category_Name.toString(),
                    "%.2f".format(item.Transaction_Amount),
                    "%.2f".format(rBalance),
                    item.Transaction_Date.format(recordTimeFormatter),
                    item.TransactionType_ID,
                    item.AccountRecipient_ID,
                    id,
                    item.Account_ID

                )
                accountTransRecordList.add(model)

                //income
                if (item.TransactionType_ID == TRANSACTION_TYPE_INCOME ||
                    (item.TransactionType_ID == TRANSACTION_TYPE_TRANSFER || item.TransactionType_ID == TRANSACTION_TYPE_DEBIT) &&
                    (item.Account_ID != id && item.AccountRecipient_ID == id) ) {

                    rBalance -= item.Transaction_Amount

                }
                //expense
                else
                    rBalance += item.Transaction_Amount
            }

        accountRecordsList.value = accountTransRecordList
    }

    fun calculateBalance(context: Context, itemId: Long): Double {
        val appDatabase = AppDatabase.getDatabase(context)
        val account = appDatabase.account().getRecordByID(itemId)
        var accountBalance :Double =  account.Account_Balance
        appDatabase.trans().getRecordsByAcctID(itemId)
            .forEach { trans ->
                accountBalance = calculateAmount(accountBalance, trans)
            }
        return accountBalance +  appDatabase.trans().getTotalSumB(itemId)
    }

}