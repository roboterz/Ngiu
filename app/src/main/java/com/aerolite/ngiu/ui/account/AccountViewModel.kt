package com.aerolite.ngiu.ui.account

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_CREDIT
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_RECEIVABLE
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.returntype.AccountSectionUiModel
import kotlin.math.roundToInt


class AccountViewModel(application: Application) : AndroidViewModel(application) {

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
                item.value.forEach {
                    // get Account Balance
                    it.Account_Balance = getBalance(context, it.Account_ID, it.Account_Balance)
                }

                // store the total sum of each account base off AccountType_ID
                val totalSum = item.value.sumOf { it.Account_Balance }

                // store the data to the Model
                val sectionModel = accountType?.let {
                    AccountSectionUiModel( it.AccountType_ID, accountType.AccountType_Name, ""+"%.2f".format(totalSum),
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

    private fun getBalance(context: Context, accountID: Long, balance: Double): Double{
        val totalAmountOfIncome = AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(accountID)
        val totalAmountOfExpense = AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(accountID)
        val totalAmountOfTransferOut = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(accountID)
        val totalAmountOfTransferIn = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(accountID)

        // Arrears
        val arrears = ( balance - totalAmountOfExpense - totalAmountOfTransferOut + totalAmountOfIncome + totalAmountOfTransferIn )

        //return get2DigitFormat(arrears).toDouble()
        return (arrears * 100).roundToInt().toDouble() / 100
    }

}