package com.example.ngiu.ui.record


import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.*
import com.example.ngiu.data.entities.returntype.AccountCount
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.*
import kotlin.collections.groupingBy
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

    var tempSaveOutAccountName: String = ""
    var tempSaveInAccountName: String = ""
    var tempSaveDebitOutAccountName: String = ""
    var tempSaveDebitInAccountName: String = ""

    // Transaction Type color and pointer
    var textViewTransactionTypeColor: Array<Int> = Array(4) {R.color.app_title_text_inactive}
    var transactionTypePointerVisible: Array<Int> = Array(4) {View.INVISIBLE}


    fun setTransactionTypeTextViewColor(transType: Long){
        //*** set transaction type textview color and pointer
        //*** base on the TransactionType const Value(need amend function if changed)
        for (i in 0..3){
            textViewTransactionTypeColor[i] = R.color.app_title_text_inactive
            transactionTypePointerVisible[i] = View.INVISIBLE
        }
        textViewTransactionTypeColor[transType.toInt()-1] = R.color.app_title_text
        transactionTypePointerVisible[transType.toInt()-1] = View.VISIBLE
    }


    fun setTransactionType(tyID: Long): CurrentTransactionType {
        currentTransactionType = currentTransactionType.setID(tyID)
        transDetail.TransactionType_ID = tyID
        return currentTransactionType
    }


    //
    fun loadDataToRam(context: Context) {
        val database = AppDatabase.getDatabase(context)
        category = database.category().getAllCategory()
        account = database.account().getAllAccount()
        expenseCommonCategory = database.category().getCommonCategoryByTransactionType(TRANSACTION_TYPE_EXPENSE)
        incomeCommonCategory = database.category().getCommonCategoryByTransactionType(TRANSACTION_TYPE_INCOME)
        transferCommonCategory = database.category().getCommonCategoryByTransactionType(TRANSACTION_TYPE_TRANSFER)
        debitCreditCommonCategory = database.category().getCommonCategoryByTransactionType(TRANSACTION_TYPE_DEBIT)

        person = database.person().getAllPerson()
        merchant = database.merchant().getAllMerchant()
        project = database.project().getAllProject()

        // default sub category name

        val expenseCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_EXPENSE)
        val incomeCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_INCOME)
        val transferCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_TRANSFER)
        val debitCreditCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_DEBIT)


        categoryName.add(if (expenseCategory.size > 1) expenseCategory[1].Category_Name else expenseCategory[0].Category_Name)
        categoryName.add(if (incomeCategory.size > 1) incomeCategory[1].Category_Name else incomeCategory[0].Category_Name)
        categoryName.add(transferCategory[0].Category_Name)
        categoryName.add(debitCreditCategory[0].Category_Name)


        tempSavedAccountName.add(if (account.isNotEmpty()) account[0].Account_Name else context.getString(R.string.msg_no_account))
        tempSavedAccountName.add(if (account.size > 1) account[1].Account_Name else context.getString(R.string.msg_no_account))
        tempSavedAccountName.add(context.getString(R.string.msg_no_account))
        for (at in account){
            if (at.AccountType_ID == ACCOUNT_TYPE_RECEIVABLE) tempSavedAccountName[2] = at.Account_Name
        }
        tempSavedAccountName.add(if (account.isNotEmpty()) account[0].Account_Name else context.getString(R.string.msg_no_account))

//        if (account.isEmpty()){
//            tempSaveOutAccountName = context.getString(R.string.msg_no_account)
//            tempSaveInAccountName = context.getString(R.string.msg_no_account)
//            tempSaveDebitOutAccountName = context.getString(R.string.msg_no_account)
//            tempSaveDebitInAccountName = context.getString(R.string.msg_no_account)
//        }else if (account.size == 1){
//            tempSaveOutAccountName = getPayOutAccountName(context, CATEGORY_MAIN_EXPENSE, TRANSACTION_TYPE_EXPENSE)
//            tempSaveInAccountName = context.getString(R.string.msg_no_account)
//            tempSaveDebitOutAccountName = tempSaveOutAccountName
//            tempSaveDebitInAccountName = context.getString(R.string.msg_no_account)
//        }else{
//            tempSaveOutAccountName = getPayOutAccountName(context, CATEGORY_MAIN_EXPENSE, TRANSACTION_TYPE_EXPENSE)
//            //tempSaveInAccountName = context.getString(R.string.msg_no_account)
//            //tempSaveDebitOutAccountName = tempSaveOutAccountName
//            //tempSaveDebitInAccountName = context.getString(R.string.msg_no_account)
//        }

    }


    fun loadTransactionDetail(context: Context, rID: Long) {
        transDetail = AppDatabase.getDatabase(context).trans().getOneTransaction(rID)
    }

    fun getSubCategoryName(): String{
        return categoryName[transDetail.TransactionType_ID.toInt() -1]
    }
    fun  setSubCategoryName(string: String){
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



    // return account name when open the record fragment with different transaction type.
    fun getPayOutAccountName(context: Context, category: Long = 0L , transType: Long = 0L, accountID: Long = 0L): String{
        // todo bug: open category manager and go back, RP default account was changed.


        if (accountID > 0 ){
            return AppDatabase.getDatabase(context).account().getRecordByID(accountID).Account_Name
        }

        val transCount = AppDatabase.getDatabase(context).trans().getTransCount()

        val accountCount: Int = when (transType){
            TRANSACTION_TYPE_DEBIT -> {
                AppDatabase.getDatabase(context).account().getAccountCountType(
                    TRANSACTION_TYPE_DEBIT)
            }
            else ->{
                AppDatabase.getDatabase(context).account().getAccountCountExcept(
                    TRANSACTION_TYPE_DEBIT)
            }
        }
        //val accountCount = AppDatabase.getDatabase(context).account().getAccountCountExcept(
        //    if (transType == TRANSACTION_TYPE_TRANSFER) 0L else ACCOUNT_TYPE_RECEIVABLE
        //    )

        //val account = AppDatabase.getDatabase(context).account().getAllAccount()

        if (accountCount == 0) {
            return context.getString(R.string.msg_no_account)

        }else{
            return if (transCount > 0) {
                var acctNameList = AppDatabase.getDatabase(context).trans()
                    .getCountOfAccountsByTransactionTypeAndCategory(transType, category)

                if (acctNameList.isEmpty()) {
                    acctNameList = AppDatabase.getDatabase(context).trans()
                        .getCountOfAccountsByTransactionType(transType)
                }

                if (acctNameList.isEmpty()){
                    transDetail.Account_Name
                }else {
                    acctNameList[0].Account_Name
                }
            }else{
                transDetail.Account_Name
            }
        }
        /*        fun findMostCommonValues(values: List<Any>): List<Any> {
            // Group the values by their count
            val groups = values.groupingBy { it }.eachCount()

            // Sort the map by count in descending order
            val sortedGroups = groups.toList().sortedByDescending { (_, count) -> count }

            // Extract the elements from the sorted map
            return sortedGroups.map { (element, _) -> element }
        }*/

    }

    // return account name list when open the record fragment with different transaction type.
    fun getAccountList(): List<String>?{
        val acctList: List<String>? = null
        // todo

        return acctList
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