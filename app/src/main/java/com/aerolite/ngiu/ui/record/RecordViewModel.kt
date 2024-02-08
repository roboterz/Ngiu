package com.aerolite.ngiu.ui.record


import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.entities.Merchant
import com.aerolite.ngiu.data.entities.Project
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_RECEIVABLE
import com.aerolite.ngiu.functions.CATEGORY_SUB_BORROW
import com.aerolite.ngiu.functions.CATEGORY_SUB_LEND
import com.aerolite.ngiu.functions.CATEGORY_SUB_PAYMENT
import com.aerolite.ngiu.functions.CATEGORY_SUB_RECEIVE_PAYMENT
import com.aerolite.ngiu.functions.NON_REIMBURSABLE
import com.aerolite.ngiu.functions.REIMBURSABLE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_DEBIT
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_TRANSFER
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Category
import com.aerolite.ngiu.data.entities.Person
import com.aerolite.ngiu.data.entities.Template
import com.aerolite.ngiu.data.entities.returntype.TransactionDetail
import kotlin.collections.ArrayList

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

     */

    private val MyDatabase = AppDatabase.getDatabase(application)

    // temp save changed data
    var transDetail = TransactionDetail(TransactionType_ID = TRANSACTION_TYPE_EXPENSE)

    private var categoryIDs = ArrayList<Long>()
    //var tempSavedAccountName = ArrayList<String>()

    var expenseCommonCategory: List<Category> = ArrayList()
    var incomeCommonCategory: List<Category> = ArrayList()
    var transferCommonCategory: List<Category> = ArrayList()
    var debitCreditCommonCategory: List<Category> = ArrayList()
    var category: List<Category> = ArrayList()
    var person: List<Person> = ArrayList()
    var merchant: List<Merchant> = ArrayList()
    var account: List<Account> = ArrayList()
    var project: List<Project> = ArrayList()
    
    var TempLateID = 0L 

    var tempSaveOutAccountName: String = ""
    var tempSaveInAccountName: String = ""
    var tempSaveDebitAccountName: String = ""



    // ********** NEW Variable **********
    // Transaction Type color and pointer
    var textViewTransactionTypeColor: Array<Int> = Array(4) {R.color.app_title_text_inactive}
    var transactionTypePointerVisible: Array<Int> = Array(4) {View.INVISIBLE}



    /************ New Function ***********/

    private fun setTransactionTypeTextViewColor(transType: Long){
        //*** set transaction type textview color and pointer
        //*** base on the TransactionType const Value(need amend function if changed)
        for (i in 0..3){
            textViewTransactionTypeColor[i] = R.color.app_title_text_inactive
            transactionTypePointerVisible[i] = View.INVISIBLE
        }
        textViewTransactionTypeColor[transType.toInt()-1] = R.color.app_title_text
        transactionTypePointerVisible[transType.toInt()-1] = View.VISIBLE
    }


    fun setTransactionType(tyID: Long) {
        if (tyID > 0) {
            transDetail.TransactionType_ID = tyID
            setTransactionTypeTextViewColor(tyID)
        }
    }

    /************ New Function ***********/



    //
    fun loadDataToRam(context: Context) {

        /** load data  **/

        val database = AppDatabase.getDatabase(context)
        category = database.category().getAllCategory()
        account = database.account().getAllAccount()
        expenseCommonCategory = database.category().getCommonCategoryByTransactionType(
            TRANSACTION_TYPE_EXPENSE
        )
        incomeCommonCategory = database.category().getCommonCategoryByTransactionType(
            TRANSACTION_TYPE_INCOME
        )
        transferCommonCategory = database.category().getCommonCategoryByTransactionType(
            TRANSACTION_TYPE_TRANSFER
        )
        debitCreditCommonCategory = database.category().getCommonCategoryByTransactionType(
            TRANSACTION_TYPE_DEBIT
        )

        person = database.person().getAllPerson()
        merchant = database.merchant().getAllMerchant()
        project = database.project().getAllProject()

        // default sub category name

        val expenseCategory = database.category().getCategoryByTransactionType(
            TRANSACTION_TYPE_EXPENSE
        )
        val incomeCategory = database.category().getCategoryByTransactionType(
            TRANSACTION_TYPE_INCOME
        )
        //val transferCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_TRANSFER)
        //val debitCreditCategory = database.category().getCategoryByTransactionType(TRANSACTION_TYPE_DEBIT)


        categoryIDs.add(if (expenseCommonCategory.size > 1) expenseCommonCategory[0].Category_ID else expenseCategory[0].Category_ID)
        categoryIDs.add(if (incomeCommonCategory.size > 1) incomeCommonCategory[0].Category_ID else incomeCategory[0].Category_ID)
        categoryIDs.add(transferCommonCategory[0].Category_ID)
        categoryIDs.add(debitCreditCommonCategory[0].Category_ID)



        // init account
        tempSaveOutAccountName = getAccountNameByCount(context, TRANSACTION_TYPE_EXPENSE)
        tempSaveInAccountName = getSecondAccountName(context, tempSaveOutAccountName)
        tempSaveDebitAccountName = getAccountNameByCount(context, TRANSACTION_TYPE_DEBIT)

    }



    private fun getSecondAccountName(context: Context, acctName: String): String {
        val acctList = AppDatabase.getDatabase(context).account().getAccountExceptType(
            ACCOUNT_TYPE_RECEIVABLE
        )
        return if (acctList.lastIndex >= 1) {
                    if (acctList[0].Account_Name == acctName){
                        acctList[1].Account_Name
                    }else {
                        acctList[0].Account_Name
                    }
                }else{
                    context.getString(R.string.msg_no_account)
                }
    }

    private fun getAccountNameByCount(context: Context, transType: Long): String{

        val transCount = AppDatabase.getDatabase(context).trans().getTransCount()

        val accountCount: Int = when (transType){
            TRANSACTION_TYPE_DEBIT -> {
                AppDatabase.getDatabase(context).account().getAccountCountType(
                    ACCOUNT_TYPE_RECEIVABLE
                )
            }
            else ->{
                AppDatabase.getDatabase(context).account().getAccountCountExcept(
                    ACCOUNT_TYPE_RECEIVABLE
                )
            }
        }

        if (accountCount == 0) {
            return context.getString(R.string.msg_no_account)

        }else{
            val acctList: List<Account> = when (transType){
                TRANSACTION_TYPE_DEBIT -> {
                    AppDatabase.getDatabase(context).account().getAccountByType(
                        ACCOUNT_TYPE_RECEIVABLE
                    )
                }
                else ->{
                    AppDatabase.getDatabase(context).account().getAccountExceptType(
                        ACCOUNT_TYPE_RECEIVABLE
                    )
                }
            }

            return if (transCount > 0) {
                val transAcctList = AppDatabase.getDatabase(context).trans()
                    .getCountOfRecipientAccountsByTransactionType(transType)

                if (transAcctList.isEmpty()){
                    acctList[0].Account_Name
                }else {
                    transAcctList[0].Account_Name
                }
            }else{
                acctList[0].Account_Name
            }
        }

    }

    fun reloadCategory(context: Context, transType: Long){
        when (transType){
            TRANSACTION_TYPE_EXPENSE -> {
               expenseCommonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(
                   TRANSACTION_TYPE_EXPENSE
               )
            }
            TRANSACTION_TYPE_INCOME -> {
                incomeCommonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(
                    TRANSACTION_TYPE_INCOME
                )
            }
            TRANSACTION_TYPE_TRANSFER -> {
                transferCommonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(
                    TRANSACTION_TYPE_TRANSFER
                )
            }
            TRANSACTION_TYPE_DEBIT -> {
                debitCreditCommonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(
                    TRANSACTION_TYPE_DEBIT
                )
            }
        }
    }

    fun loadTransactionDetail(context: Context, rID: Long, templateID: Long ) {
        // load transaction data
        if (templateID > 0L){
            setTransactionDetailFromTemplate(templateID)
        }else{
            transDetail = AppDatabase.getDatabase(context).trans().getOneTransactionDetail(rID)
        }

        // category
        categoryIDs[transDetail.TransactionType_ID.toInt()-1] = transDetail.Category_ID
        // account
        when (transDetail.TransactionType_ID){
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME ->{
                tempSaveOutAccountName = transDetail.Account_Name
                tempSaveInAccountName = getSecondAccountName(context, tempSaveOutAccountName)
                transDetail.AccountRecipient_Name = tempSaveInAccountName
            }
            TRANSACTION_TYPE_TRANSFER ->{
                tempSaveOutAccountName = transDetail.Account_Name
                tempSaveInAccountName = transDetail.AccountRecipient_Name
            }
            TRANSACTION_TYPE_DEBIT ->{
                when (getCategoryID(transDetail.Category_Name)) {
                    CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                        tempSaveDebitAccountName = transDetail.Account_Name
                        tempSaveOutAccountName = transDetail.AccountRecipient_Name
                        tempSaveInAccountName = getSecondAccountName(context, tempSaveOutAccountName)
                    }
                    CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> {
                        tempSaveOutAccountName = transDetail.Account_Name
                        tempSaveDebitAccountName = transDetail.AccountRecipient_Name
                        tempSaveInAccountName = getSecondAccountName(context, tempSaveOutAccountName)
                    }
                }
            }
        }

        // color
        setTransactionTypeTextViewColor(transDetail.TransactionType_ID)
    }

    private fun getSubCategoryName(cateID: Long): String{
        val idx = category.indexOfFirst { it.Category_ID == cateID }
        return if (idx >= 0) category[idx].Category_Name
        else ""
    }

    fun getAccountNameByID(acctID: Long): String{
        return account[account.indexOfFirst { it.Account_ID == acctID }].Account_Name
    }
    fun setSubCategory(transType: Long, cateID: Long =0L){
        if (cateID > 0L){
            categoryIDs[transType.toInt()-1] = cateID
        }
        transDetail.Category_ID = categoryIDs[transType.toInt()-1]
        transDetail.Category_Name =  getSubCategoryName(transDetail.Category_ID)
    }



    fun setAccountName(transType: Long, acctName: String ="", blnOut: Boolean = true){
        when(transType){
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME -> {
                if (acctName.isNotEmpty()) {
                    tempSaveOutAccountName = acctName
                }
            }

            TRANSACTION_TYPE_TRANSFER ->{
                if (acctName.isNotEmpty()) {
                    if (blnOut) {
                        tempSaveOutAccountName = acctName
                    }else{
                        tempSaveInAccountName = acctName
                    }
                }
            }

            TRANSACTION_TYPE_DEBIT -> {
                if (acctName.isNotEmpty()) {

                    when (getCategoryID(transDetail.Category_Name)) {
                        CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                            if (blnOut) {
                                tempSaveDebitAccountName = acctName
                            }else{
                                tempSaveOutAccountName = acctName
                            }
                        }
                        CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> {
                            if (blnOut) {
                                tempSaveOutAccountName = acctName
                            }else{
                                tempSaveDebitAccountName = acctName
                            }
                        }
                    }
                }
            }

        }

        // set account name
        when(transType){
            TRANSACTION_TYPE_EXPENSE, TRANSACTION_TYPE_INCOME, TRANSACTION_TYPE_TRANSFER -> {
                transDetail.Account_Name = tempSaveOutAccountName
                transDetail.AccountRecipient_Name = tempSaveInAccountName
            }
            TRANSACTION_TYPE_DEBIT ->{
                when (getCategoryID(transDetail.Category_Name)) {
                    CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> {
                        transDetail.Account_Name = tempSaveDebitAccountName
                        transDetail.AccountRecipient_Name = tempSaveOutAccountName
                    }
                    CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> {
                        transDetail.Account_Name = tempSaveOutAccountName
                        transDetail.AccountRecipient_Name = tempSaveDebitAccountName
                    }
                }
            }
        }

        // set account ID
        transDetail.Account_ID = getAccountID(transDetail.Account_Name)
        transDetail.AccountRecipient_ID = getAccountID(transDetail.AccountRecipient_Name)
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
                    when (transDetail.Category_ID) {
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
                when (transDetail.Category_ID) {
                    // borrow in | received
                    CATEGORY_SUB_BORROW, CATEGORY_SUB_RECEIVE_PAYMENT -> if (payAccount) nameList.add(at.Account_Name)
                    // lend out | repayment
                    CATEGORY_SUB_LEND, CATEGORY_SUB_PAYMENT -> if (!payAccount) nameList.add(at.Account_Name)
                }
            }
        }
        return nameList.toTypedArray()

    }

    fun setTransactionDetailFromTemplate(templateID: Long) {
        val template = MyDatabase.template().getOneTemplateDetailByID(templateID)

        TempLateID = templateID

        transDetail.Transaction_ID = 0L
        transDetail.TransactionType_ID = template.TransactionType_ID
        transDetail.Account_ID = template.Account_ID
        transDetail.Account_Name = template.Account_Name
        transDetail.AccountRecipient_ID = template.AccountRecipient_ID
        transDetail.AccountRecipient_Name = template.AccountRecipient_Name
        transDetail.Category_ID = template.Category_ID
        transDetail.Category_Name = template.Category_Name
        transDetail.Transaction_Amount = template.Transaction_Amount
        transDetail.Transaction_Amount2 = 0.00
        transDetail.Merchant_ID = template.Merchant_ID
        transDetail.Merchant_Name = template.Merchant_Name
        transDetail.Person_ID = template.TransactionType_ID
        transDetail.Person_Name = template.Person_Name
        transDetail.Project_ID = template.Project_ID
        transDetail.Project_Name = template.Project_Name
        transDetail.Period_ID = 0L
        transDetail.Transaction_Memo = template.Transaction_Memo
        transDetail.Transaction_ReimburseStatus = template.Transaction_ReimburseStatus
        //transDetail.Transaction_Date =

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

    fun saveTemplate(context: Context, template: Template){
        AppDatabase.getDatabase(context).template().addTemplate(template)
    }

    fun deleteTemplate(context: Context, template: Template){
        AppDatabase.getDatabase(context).template().deleteTemplate(template)
    }

}


