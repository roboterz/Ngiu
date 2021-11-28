package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.ui.account.model.AccountSectionUiModel


class AccountViewModel : ViewModel() {

    val accountSections = MutableLiveData<List<AccountSectionUiModel>>()

    var balance: Double = 0.0


    fun getAccountSectionUiModal(context: Context){
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.accounttype().getAllAccountTypes()
        val allAccounts = appDatabase.account().getAllAccount()
        val sections = ArrayList<AccountSectionUiModel>()
        allAccounts.groupBy { it.AccountType_ID }
            .forEach { item->
                val accountType = allTypes.find { it.AccountType_ID ==  item.key}
                item.value.forEach {
                    // val sum1 = appDatabase.trans().getTotalSumA(it.Account_ID)
                    val sum2 = appDatabase.trans().getTotalSumB(it.Account_ID)
                    it.Account_Balance += (sum2)

                    appDatabase.trans().getRecordsByAcctID(it.Account_ID)
                        .forEach{ tran->
                            it.Account_Balance = calculateAmount(it.Account_Balance,tran)
                        }
                }
                val totalSum = item.value.sumOf { it.Account_Balance }
                val sectionModel = AccountSectionUiModel(accountType?.AccountType_Name.orEmpty(), "%.2f".format(totalSum),true, item.value)
                sections.add(sectionModel)

            }
        accountSections.value = sections
    }

    fun calculateAmount(balance: Double, tran: Trans): Double{
        return when(tran.TransactionType_ID){
            in arrayOf<Long>(1,3)-> balance - tran.Transaction_Amount
            in arrayOf<Long>(2,4)-> balance + tran.Transaction_Amount
            else -> balance
        }
    }
}