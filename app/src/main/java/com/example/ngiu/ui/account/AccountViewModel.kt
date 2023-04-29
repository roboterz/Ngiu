package com.example.ngiu.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.functions.*
import com.example.ngiu.functions.calculateAmount
import com.example.ngiu.ui.account.model.AccountSectionUiModel


class AccountViewModel : ViewModel() {

    val accountSections = MutableLiveData<List<AccountSectionUiModel>>()


    // adding because if liability is negative number would add if, subtraction
    fun getNetAssets(): Double {
        val liability = getTotalLiability()
        val totalAssets = getTotalAssets()

        return totalAssets + liability
    }

    // if account has negative balance, sum the value for liability
    fun getTotalLiability(): Double{
        var sum = 0.0
        accountSections.value?.forEach { it ->
            sum += it.list.filter {
                it.Account_Balance<0
                it.AccountType_ID == ACCOUNT_TYPE_CREDIT || it.AccountType_ID == ACCOUNT_TYPE_RECEIVABLE
            }.sumOf { it.Account_Balance }
        }

        return sum


    }

    // get the Account_Balance where count in net assets is true/1
    // getTransactionSums(1) = Expense
    // getTransactionSums(2) = Income
    fun getTotalAssets(): Double {
        /*
        val appDatabase = AppDatabase.getDatabase(context)

        return appDatabase.trans().getSumOfAccountBalance() + appDatabase.trans()
            .getSumOfAmountByTransactionType(2) - appDatabase.trans().getSumOfAmountByTransactionType(1) + appDatabase.trans()
            .getSumOfAmountForPayableReceivable()
         */

        var sum = 0.0
        accountSections.value?.forEach { it ->
            sum += it.list.filter {
                it.Account_Balance > 0
            }.sumOf { it.Account_Balance }
        }

        return sum

    }


    fun getAccountSectionUiModel(context: Context){
        val appDatabase = AppDatabase.getDatabase(context)
        val allTypes = appDatabase.accountType().getAllAccountType()
        val allAccounts = appDatabase.account().getAllAccountASC()
        val sections = ArrayList<AccountSectionUiModel>()
        // group the AccountType_ID; setting key,value
        // key:value = AccountType_ID : list of all accounts with same AccountType_ID
        allAccounts.groupBy { it.AccountType_ID }
            .forEach { item->
                // search the list of account type table to match with AccountType_ID
                val accountType = allTypes.find { it.AccountType_ID ==  item.key}

                // Individual accounts calculation
                // get all the transaction base off account_ID and aggregate the new balance
                item.value.forEach {
                    // gets the sum of AccountRecipient_ID == Account_ID
                    val sum2 = appDatabase.trans().getTotalSumB(it.Account_ID)
                    it.Account_Balance += sum2

                    // get the list transaction base off Account_ID
                    appDatabase.trans().getRecordsByAcctID(it.Account_ID)
                        .forEach{ tran->
                            // for each transaction calculate the new amount and set it to account_balance
                            it.Account_Balance = calculateAmount(it.Account_Balance,tran)
                        }
                }
                // store the total sum of each account base off AccountType_ID
                val totalSum = item.value.sumOf { it.Account_Balance }

                // store the data to the Model
                val sectionModel = accountType?.let {
                    AccountSectionUiModel( it.AccountType_ID, accountType.AccountType_Name, "$"+"%.2f".format(totalSum),
                        it.AccountType_Expanded, item.value)
                }
                if (sectionModel != null) {
                    sections.add(sectionModel)
                }

            }
        accountSections.value = sections
    }

    fun saveExpandedStatus(context: Context, accountType_ID: Long, isExpanded: Boolean){
        AppDatabase.getDatabase(context).accountType().updateExpandedValueByID(accountType_ID, isExpanded)
    }


}