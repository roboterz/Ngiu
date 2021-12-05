package com.example.ngiu.ui.record


import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.*
import com.example.ngiu.data.entities.returntype.TransactionDetail
import kotlinx.android.synthetic.main.fragment_record.*

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
    var transDetail = TransactionDetail(TransactionType_ID = 1L)

    var subCategoryName = ArrayList<String>()

    var expenseCommonCategory: List<SubCategory> = ArrayList()
    var incomeCommonCategory: List<SubCategory> = ArrayList()
    var transferCommonCategory: List<SubCategory> = ArrayList()
    var debitCreditCommonCategory: List<SubCategory> = ArrayList()
    var subCategory: List<SubCategory> = ArrayList()
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
        subCategory = database.subcat().getAllSubCategory()
        account = database.account().getAllAccount()
        expenseCommonCategory = database.subcat().getCommonCategoryByTransactionType(1L)
        incomeCommonCategory = database.subcat().getCommonCategoryByTransactionType(2L)
        transferCommonCategory = database.subcat().getCommonCategoryByTransactionType(3L)
        debitCreditCommonCategory = database.subcat().getCommonCategoryByTransactionType(4L)

        person = database.person().getAllPerson()
        merchant = database.merchant().getAllMerchant()
        project = database.project().getAllProject()

        // default sub category name

        val expenseCategory = database.subcat().getSubCategoryByTransactionType(1L)
        val incomeCategory = database.subcat().getSubCategoryByTransactionType(2L)
        val transferCategory = database.subcat().getSubCategoryByTransactionType(3L)
        val debitCreditCategory = database.subcat().getSubCategoryByTransactionType(4L)


        subCategoryName.add(if (expenseCategory.size > 1) expenseCategory[1].SubCategory_Name else expenseCategory[0].SubCategory_Name)
        subCategoryName.add(if (incomeCategory.size > 1) incomeCategory[1].SubCategory_Name else incomeCategory[0].SubCategory_Name)
        subCategoryName.add(transferCategory[0].SubCategory_Name)
        subCategoryName.add(debitCreditCategory[0].SubCategory_Name)

        //transDetail.TransactionType_ID = 1L
    }

    //fun getOneSubCategory(activity: FragmentActivity?, rID: Long): SubCategory {
    //    return AppDatabase.getDatabase(activity!!).subcat().getRecordByID(rID)
    //}


    fun loadTransactionDetail(activity: FragmentActivity?, rID: Long) {
        transDetail = AppDatabase.getDatabase(activity!!).trans().getOneTransaction(rID)
    }

    fun getSubCategoryName(): String{
        return subCategoryName[transDetail.TransactionType_ID.toInt() -1]
    }
    fun setSubCategoryName(string: String){
        subCategoryName[transDetail.TransactionType_ID.toInt() -1] = string
    }

    // get reimburse status
    fun getReimbursable(context: Context, int: Int):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        return array[int]
    }
    fun setReimbursable(context: Context):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        return when(transDetail.Transaction_ReimburseStatus){
            0,1 -> {
                transDetail.Transaction_ReimburseStatus++
                array[transDetail.Transaction_ReimburseStatus]
            }
            else -> {
                transDetail.Transaction_ReimburseStatus = 0
                array[0]
            }
        }
    }

    // get account name list
    fun getAccountList(): List<String>{
        val tList: MutableList<String> = ArrayList<String>()
        for (account in account){
            tList.add(account.Account_Name)
        }
        return tList
    }

    fun getSubCategoryID(string: String): Long{
        val idx = subCategory.indexOfFirst { it.SubCategory_Name == string }
        return if (idx >= 0) subCategory[idx].SubCategory_ID
            else idx.toLong()
    }

    fun getAccountID(string: String): Long{
        val idx = account.indexOfFirst{it.Account_Name == string}
        return if (idx >= 0) account[idx].Account_ID
            else idx.toLong()
    }

    fun getListOfAccountName(exceptName:String, payAccount: Boolean): Array<String>{
        val nameList: ArrayList<String> = ArrayList()

        for (at in account) {
            // Normal Account
            if (at.AccountType_ID != 9L) {
                if ((transDetail.TransactionType_ID != 3L) || (at.Account_Name != exceptName))
                    nameList.add(at.Account_Name)
            // Payable|Receivable Account
            }else if (transDetail.TransactionType_ID == 4L) {
                when (getSubCategoryID(transDetail.SubCategory_Name)) {
                    // borrow in | received
                    7L, 10L -> if (payAccount) nameList.add(at.Account_Name)
                    // lend out | repayment
                    8L, 9L -> if (!payAccount) nameList.add(at.Account_Name)
                }
            }
        }
        return nameList.toTypedArray()
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
            1L -> {
                cTT.expense = R.color.app_title_text
                cTT.expensePointer = View.VISIBLE
            }
            2L -> {
                cTT.income = R.color.app_title_text
                cTT.incomePointer = View.VISIBLE
            }
            3L -> {
                cTT.transfer = R.color.app_title_text
                cTT.transferPointer = View.VISIBLE
            }
            4L -> {
                cTT.debitCredit = R.color.app_title_text
                cTT.debitCreditPointer = View.VISIBLE
            }
        }

        return cTT
    }


}