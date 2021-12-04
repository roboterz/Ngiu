package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.ui.account.model.AccountSectionUiModel


class AccountViewModel : ViewModel() {

    val accountSections = MutableLiveData<List<AccountSectionUiModel>>()


    // adding because if liability is negative number would add if, subtraction
    fun getNetAssets(context:Context): Double {
        val liability = getTotalLiability()
        val totalAssets = getTotalAssets(context)

        return totalAssets + liability
    }

    // if account has negative balance, sum the value for liability
    fun getTotalLiability(): Double{
        var sum = 0.0
        accountSections.value?.forEach {
            sum += it.list.filter { it.Account_Balance<0 }.sumOf { it.Account_Balance }
        }
        return sum


    }

    // get the Account_Balance where count in net assets is true/1
    // getTransactionSums(1) = Expense
    // getTransactionSums(2) = Income
    fun getTotalAssets(context: Context): Double {
        val appDatabase = AppDatabase.getDatabase(context)

        return appDatabase.trans().getSumAccount() + appDatabase.trans()
            .getTransactionSums(2) - appDatabase.trans().getTransactionSums(1) + appDatabase.trans()
            .getSumTotalAsset()
    }


    fun getAccountSectionUiModel(context: Context){
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.accounttype().getAllAccountTypes()
        val allAccounts = appDatabase.account().getAllAccount()
        val sections = ArrayList<AccountSectionUiModel>()
        // group the AccountType_ID; setting key,value
        allAccounts.groupBy { it.AccountType_ID }
            .forEach { item->
                // search the list of account type table to match with a accounttype_id
                val accountType = allTypes.find { it.AccountType_ID ==  item.key}
                // add the value for all the same accounttype_id
                item.value.forEach {
                    // val sum1 = appDatabase.trans().getTotalSumA(it.Account_ID)
                    val sum2 = appDatabase.trans().getTotalSumB(it.Account_ID)
                    it.Account_Balance += sum2

                    // get the list transaction base off Account_ID
                    appDatabase.trans().getRecordsByAcctID(it.Account_ID)
                        .forEach{ tran->
                            // for each transaction calculate the new amount and set it to account_balance
                            it.Account_Balance = calculateAmount(it.Account_Balance,tran)
                        }
                }
                // store the total sum of each account
                val totalSum = item.value.sumOf { it.Account_Balance }
                // store the data to the Model
                val sectionModel = AccountSectionUiModel(accountType?.AccountType_Name.orEmpty(), "%.2f".format(totalSum),true, item.value)
                sections.add(sectionModel)

            }
        accountSections.value = sections
    }

    // balance is the account original balance
    // add/subtract base off transaction type ID
    fun calculateAmount(balance: Double, tran: Trans): Double{
        return when(tran.TransactionType_ID){
            in arrayOf<Long>(1,3)-> balance - tran.Transaction_Amount
            in arrayOf<Long>(2,4)-> balance + tran.Transaction_Amount
            else -> balance
        }
    }
}