package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.ui.account.model.AccountSectionUiModel
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
        val allTypes = appDatabase.subcat().getAllSubCategory()
        val allRecords = appDatabase.trans().getTransRecordAccount(id).asReversed()

        val accountTransRecordList = ArrayList<AccountTransRecordModel>()

        var rBalance = balance


        allRecords
            .forEach { item ->
                val subCat = allTypes.find { it.SubCategory_ID == item.SubCategory_ID }


                if (item.TransactionType_ID == 2L ) {
                    color = 0
                }
                //expense
                else if (item.TransactionType_ID == 1L ){
                    color = 1

                } else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID == id && item.AccountRecipient_ID != id)) {
                    color = 0

                }
                else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID != id && item.AccountRecipient_ID == id)) {
                    color = 1

                }



                val model = AccountTransRecordModel(
                    subCat?.SubCategory_Name.toString(),
                    "%.2f".format(item.Transaction_Amount),
                    "%.2f".format(rBalance),
                    item.Transaction_Date.format(recordTimeFormatter),
                    item.TransactionType_ID,
                    color

                )
                accountTransRecordList.add(model)

                //income
                if (item.TransactionType_ID == 2L ) {
                    rBalance -= item.Transaction_Amount
                    color = 0
                }
                //expense
                else if (item.TransactionType_ID == 1L ){
                    rBalance += item.Transaction_Amount
                    color = 1

                } else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID == id && item.AccountRecipient_ID != id)) {
                    rBalance += item.Transaction_Amount
                    color = 0

                }
                else if ((item.TransactionType_ID == 3L || item.TransactionType_ID == 4L) && (item.Account_ID != id && item.AccountRecipient_ID == id)) {
                    rBalance -= item.Transaction_Amount
                    color = 1

                }




            }

        accountRecordsList.value = accountTransRecordList
    }
}