package com.example.ngiu.ui.record


import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.*
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.*

import kotlin.collections.ArrayList

class RecordViewModel : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

     */

    var currentTransactionType: CurrentTransactionType = CurrentTransactionType()

    // temp save changed data
    var transDetail = TransactionDetail(TransactionType_ID = TRANSACTION_TYPE_EXPENSE)

    private var categoryName = ArrayList<String>()
    var tempSavedAccountName = ArrayList<String>()

    var expenseCommonCategory: List<Category> = ArrayList()
    var incomeCommonCategory: List<Category> = ArrayList()
    var transferCommonCategory: List<Category> = ArrayList()
    var debitCreditCommonCategory: List<Category> = ArrayList()
    var category: List<Category> = ArrayList()
    var person: List<Person> = ArrayList()
    var merchant: List<Merchant> = ArrayList()
    var account: List<Account> = ArrayList()
    var project: List<Project> = ArrayList()



    fun setTransactionType(tyID: Long): CurrentTransactionType {
        currentTransactionType = currentTransactionType.setID(tyID)
        transDetail.TransactionType_ID = tyID
        return currentTransactionType
    }


    //
    fun loadDataToRam(activity: FragmentActivity?) {
        val database = AppDatabase.getDatabase(activity!!)
        category = database.category().getAllCategory()
        account = database.account().getAllAccount()
        expenseCommonCategory = database.category().getCommonCategoryByTransactionType(1L)
        incomeCommonCategory = database.category().getCommonCategoryByTransactionType(2L)
        transferCommonCategory = database.category().getCommonCategoryByTransactionType(3L)
        debitCreditCommonCategory = database.category().getCommonCategoryByTransactionType(4L)

        person = database.person().getAllPerson()
        merchant = database.merchant().getAllMerchant()
        project = database.project().getAllProject()

        // default sub category name

        val expenseCategory = database.category().getCategoryByTransactionType(1L)
        val incomeCategory = database.category().getCategoryByTransactionType(2L)
        val transferCategory = database.category().getCategoryByTransactionType(3L)
        val debitCreditCategory = database.category().getCategoryByTransactionType(4L)


        categoryName.add(if (expenseCategory.size > 1) expenseCategory[1].Category_Name else expenseCategory[0].Category_Name)
        categoryName.add(if (incomeCategory.size > 1) incomeCategory[1].Category_Name else incomeCategory[0].Category_Name)
        categoryName.add(transferCategory[0].Category_Name)
        categoryName.add(debitCreditCategory[0].Category_Name)


        tempSavedAccountName.add(if (account.isNotEmpty()) account[0].Account_Name else activity.getString(R.string.msg_no_account))
        tempSavedAccountName.add(if (account.size > 1) account[1].Account_Name else activity.getString(R.string.msg_no_account))
        tempSavedAccountName.add(activity.getString(R.string.msg_no_account))
        for (at in account){
            if (at.AccountType_ID == ACCOUNT_TYPE_RECEIVABLE) tempSavedAccountName[2] = at.Account_Name
        }
        tempSavedAccountName.add(if (account.isNotEmpty()) account[0].Account_Name else activity.getString(R.string.msg_no_account))

    }


    fun loadTransactionDetail(activity: FragmentActivity?, rID: Long) {
        transDetail = AppDatabase.getDatabase(activity!!).trans().getOneTransaction(rID)
    }

    fun getSubCategoryName(): String{
        return categoryName[transDetail.TransactionType_ID.toInt() -1]
    }
    fun setSubCategoryName(string: String){
        categoryName[transDetail.TransactionType_ID.toInt() -1] = string
    }

    fun getAccountName(payAccount: Boolean): String{
        return if (transDetail.TransactionType_ID == TRANSACTION_TYPE_DEBIT){
                    tempSavedAccountName[if (payAccount) 3 else 2 ]
                }else{
                    tempSavedAccountName[if (payAccount) 1 else 0 ]
                }
    }
    fun setAccountName(payAccount: Boolean, string: String){
        if (transDetail.TransactionType_ID == TRANSACTION_TYPE_DEBIT){
            tempSavedAccountName[if (payAccount) 2 else 3 ] = string
        }else{
            tempSavedAccountName[if (payAccount) 0 else 1 ] = string
        }
    }

    // get reimburse status
    fun getReimbursable(context: Context, int: Int):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        return array[int]
    }
    fun setReimbursable(context: Context):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        return when(transDetail.Transaction_ReimburseStatus){
            NON_REIMBURSABLE, REIMBURSABLE -> {
                transDetail.Transaction_ReimburseStatus++
                array[transDetail.Transaction_ReimburseStatus]
            }
            else -> {
                transDetail.Transaction_ReimburseStatus = 0
                array[0]
            }
        }
    }

    /*
    // get P/R account name list
    fun getPRAccountList(): List<String>{
        val tList: MutableList<String> = ArrayList<String>()
        for (at in account){
            if (at.AccountType_ID == 9L)
                tList.add(at.Account_Name)
        }
        return tList
    }

     */

    fun getCategoryID(string: String): Long{
        val idx = category.indexOfFirst { it.Category_Name == string }
        return if (idx >= 0) category[idx].Category_ID
            else idx.toLong()
    }

    fun getAccountID(string: String): Long{
        val idx = account.indexOfFirst{it.Account_Name == string}
        return if (idx >= 0) account[idx].Account_ID
            else idx.toLong()
    }

    fun getListOfAccountName(exceptName:String, payAccount: Boolean): Array<String> {
        val nameList: ArrayList<String> = ArrayList()

        // todo need rewrite
        for (at in account) {
            // Normal Account
            if (at.AccountType_ID != ACCOUNT_TYPE_RECEIVABLE) {
                if ((transDetail.TransactionType_ID != TRANSACTION_TYPE_TRANSFER) || (at.Account_Name != exceptName)) {
                    when (getCategoryID(transDetail.Category_Name)) {
                        // borrow in | received
                        CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> if (!payAccount) nameList.add(at.Account_Name)
                        // lend out | repayment
                        CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> if (payAccount) nameList.add(at.Account_Name)
                        // not transaction type 4
                        else -> nameList.add(at.Account_Name)
                    }
                }
                // Payable|Receivable Account
            } else if (transDetail.TransactionType_ID == TRANSACTION_TYPE_DEBIT) {
                when (getCategoryID(transDetail.Category_Name)) {
                    // borrow in | received
                    CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> if (payAccount) nameList.add(at.Account_Name)
                    // lend out | repayment
                    CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> if (!payAccount) nameList.add(at.Account_Name)
                }
            }
        }
        return nameList.toTypedArray()

    }

    fun updateTransaction(context: Context ,trans: Trans){
        AppDatabase.getDatabase(context).trans().updateTransaction(trans)
    }

    fun addTransaction(context: Context ,trans: Trans){
        AppDatabase.getDatabase(context).trans().addTransaction(trans)
    }

    fun deleteTrans(context: Context ,trans: Trans){
        AppDatabase.getDatabase(context).trans().deleteTransaction(trans)
    }

}



class CurrentTransactionType {
    var expense: Int = R.color.app_title_text_inactive
    var expensePointer: Int = View.INVISIBLE
    var income: Int = R.color.app_title_text_inactive
    var incomePointer: Int = View.INVISIBLE
    var transfer: Int = R.color.app_title_text_inactive
    var transferPointer: Int = View.INVISIBLE
    var debitCredit: Int = R.color.app_title_text_inactive
    var debitCreditPointer: Int = View.INVISIBLE

    fun setID(tyID: Long): CurrentTransactionType {
        val cTT = CurrentTransactionType()

        when (tyID) {
            TRANSACTION_TYPE_EXPENSE -> {
                cTT.expense = R.color.app_title_text
                cTT.expensePointer = View.VISIBLE
            }
            TRANSACTION_TYPE_INCOME -> {
                cTT.income = R.color.app_title_text
                cTT.incomePointer = View.VISIBLE
            }
            TRANSACTION_TYPE_TRANSFER -> {
                cTT.transfer = R.color.app_title_text
                cTT.transferPointer = View.VISIBLE
            }
            TRANSACTION_TYPE_DEBIT -> {
                cTT.debitCredit = R.color.app_title_text
                cTT.debitCreditPointer = View.VISIBLE
            }
        }

        return cTT
    }



}