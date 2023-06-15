package com.example.ngiu.ui.account.list


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.functions.*
import com.example.ngiu.functions.calculateAmount
import com.example.ngiu.ui.account.model.AccountSectionUiModel
import kotlin.math.roundToInt


class AccountListViewModel : ViewModel() {

    val accountListSections = MutableLiveData<List<AccountSectionUiModel>>()

    var trans_type_id = TRANSACTION_TYPE_EXPENSE
    var cate_id = 0L
    var bln_acct_out = true
    var except_id = 0L


    fun setAccountSectionUiModel(context: Context, transTypeID: Long, cateID: Long, exceptAcctID: Long, blnPay: Boolean){

        val allTypes = AppDatabase.getDatabase(context).accountType().getAllAccountType()
        val allAccounts = AppDatabase.getDatabase(context).account().getAllAccountASC()
        val sections = ArrayList<AccountSectionUiModel>()



        val accounts = filterListOfAccount(allAccounts,transTypeID,cateID, exceptAcctID, blnPay )


        // group the AccountType_ID; setting key,value
        // key:value = AccountType_ID : list of all accounts with same AccountType_ID
        accounts.groupBy { it.AccountType_ID }
            .forEach { item->
                // search the list of account type table to match with AccountType_ID
                val accountType = allTypes.find { it.AccountType_ID ==  item.key}


                // store the data to the Model
                val sectionModel = accountType?.let {
                    AccountSectionUiModel( it.AccountType_ID, accountType.AccountType_Name, "",
                        true, item.value)
                }
                if (sectionModel != null) {
                    sections.add(sectionModel)
                }

            }
        accountListSections.value = sections
    }



    private fun filterListOfAccount(accounts: List<Account>, transTypeID: Long, cateID: Long, exceptID: Long, payAccount: Boolean = true): MutableList<Account> {

        val acctList: MutableList<Account> = accounts.toMutableList()


        // remove exceptID
        if (transTypeID > TRANSACTION_TYPE_INCOME ){
            acctList.removeIf{it.Account_ID == exceptID}
        }


        // remove assign type of account base on transaction type and category type
        when (transTypeID) {
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME, TRANSACTION_TYPE_TRANSFER ->{
                acctList.removeIf{ it.AccountType_ID == ACCOUNT_TYPE_RECEIVABLE }
            }
            TRANSACTION_TYPE_DEBIT ->{
                when (cateID) {
                    CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT ->{
                        acctList.removeIf{ (it.AccountType_ID != ACCOUNT_TYPE_RECEIVABLE) == payAccount}
                    }
                    CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT ->{
                        acctList.removeIf{(it.AccountType_ID == ACCOUNT_TYPE_RECEIVABLE) == payAccount}
                    }
                }
            }
        }


        return acctList

    }

}