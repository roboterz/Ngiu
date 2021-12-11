package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.ui.account.model.AccountTransRecordModel
import java.time.format.DateTimeFormatter

class AccountCreditDetailViewModel: ViewModel() {
    val accountRecordsList = MutableLiveData<List<AccountTransRecordModel>>()
    private val recordTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm")



   /* fun getTransRecords(context: Context, id: Long, balance: Double) {
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.subcat().getAllSubCategory()
        val allRecords = appDatabase.trans().getTransRecordAccount(id).asReversed()

        val accountTransRecordList = ArrayList<AccountTransRecordModel>()

        var rBalance = balance


        allRecords
            .forEach { item ->
                val subCat = allTypes.find { it.SubCategory_ID == item.SubCategory_ID }

                val model = AccountTransRecordModel(
                    subCat?.SubCategory_Name.toString(),
                    "%.2f".format(item.Transaction_Amount),
                    "%.2f".format(rBalance),
                    item.Transaction_Date.format(recordTimeFormatter),
                    item.TransactionType_ID,
                )
                accountTransRecordList.add(model)
                //income
                if (item.TransactionType_ID == 2L ) {
                    rBalance -= item.Transaction_Amount

                }
                //expense
                else if (item.TransactionType_ID == 1L ){
                    rBalance += item.Transaction_Amount

                } else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID == id && item.AccountRecipient_ID != id)) {
                    rBalance += item.Transaction_Amount

                }
                else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID != id && item.AccountRecipient_ID == id)) {
                    rBalance -= item.Transaction_Amount

                }




            }

        accountRecordsList.value = accountTransRecordList
    }*/
}