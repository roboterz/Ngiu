package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.ui.account.model.AccountSectionUiModel
import com.example.ngiu.ui.account.model.AccountTransRecordModel

class AccountRecordsViewModel : ViewModel() {

    val accountRecordsList = MutableLiveData<List<AccountTransRecordModel>>()

    fun getInflow(context: Context, id: Long): Double {
        val appDatabase = AppDatabase.getDatabase(context)
        val inflowA = appDatabase.account().getInflowA(id)
        val inflowB = appDatabase.account().getInflowB(id)
        return inflowA + inflowB
    }

    fun getOutflow(context: Context, id: Long): Double {
        val appDatabase = AppDatabase.getDatabase(context)
        val outflowA = appDatabase.account().getOutflowA(id)
        val outflowB = appDatabase.account().getOutflowB(id)
        return outflowA + outflowB

    }

    fun getTransRecords(context: Context, id: Long, balance: Double) {
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.subcat().getAllSubCategory()
        val allRecords = appDatabase.trans().getTransRecordAccount(id).asReversed()
        val allAccounts = appDatabase.account().getAllAccount()
        val accountTransRecordList = ArrayList<AccountTransRecordModel>()

        var rBalance = balance;


        allRecords
            .forEach { item ->
                // search the list of account type table to match with a accounttype_id
                val subCat = allTypes.find { it.SubCategory_ID == item.SubCategory_ID }

                val account = allAccounts.find { it.Account_ID == item.Account_ID }



                //name, trans_amout, balance, date
                val model = AccountTransRecordModel(
                    subCat?.SubCategory_Name.toString(),
                    item.Transaction_Amount.toString(),
                   rBalance.toString(),
                    item.Transaction_Date.toString()
                )
                accountTransRecordList.add(model)

                //income
                if (item.TransactionType_ID == 2L ) {
                    rBalance = rBalance - item.Transaction_Amount
                }
                //expense
                else if (item.TransactionType_ID == 1L ){
                    rBalance = rBalance + item.Transaction_Amount
                } else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID == id && item.AccountRecipient_ID != id) ) {
                    rBalance = rBalance + item.Transaction_Amount
                }
                else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID != id && item.AccountRecipient_ID == id) ) {
                    rBalance = rBalance - item.Transaction_Amount
                }



            }

        accountRecordsList.value = accountTransRecordList
    }
}