package com.example.ngiu.ui.record


import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.*
import com.example.ngiu.data.entities.returntype.RecordSubCategory
import com.example.ngiu.data.entities.returntype.TransactionDetail
import java.util.*
import java.util.Collections.copy
import kotlin.collections.ArrayList

class RecordViewModel : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

     */

    var currentTransactionType: CurrentTransactionType = CurrentTransactionType()

    var recordOpenOption: Int = 0

    var currentRowID: Long = 0

    var transRecord = Trans()
    var transDetail = TransactionDetail()

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


    fun setTransactionType(tyID: Int): CurrentTransactionType {
        currentTransactionType = currentTransactionType.setID(tyID)
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

        subCategoryName.add(expenseCategory.SubCategory_Name)
        subCategoryName.add(incomeCategory.SubCategory_Name)
        subCategoryName.add(transferCategory.SubCategory_Name)
        subCategoryName.add(debitCreditCategory.SubCategory_Name)

    }

    //fun getOneSubCategory(activity: FragmentActivity?, rID: Long): SubCategory {
    //    return AppDatabase.getDatabase(activity!!).subcat().getRecordByID(rID)
    //}

    fun loadTransactionRecord(activity: FragmentActivity?, rID: Long) {
        transRecord = AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadTransactionDetail(activity: FragmentActivity?, rID: Long) {
        transDetail = AppDatabase.getDatabase(activity!!).trans().getOneTransaction(rID)
    }

    fun getSubCategoryName(): String{
        return subCategoryName[currentTransactionType.currentTyID -1]
    }
    fun setSubCategoryName(string: String){
        subCategoryName[currentTransactionType.currentTyID -1] = string
    }

    // get reimburse status
    fun getReimbursable(context: Context, int: Int):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        return array[int]
    }
    fun setReimbursable(context: Context):String{
        val array: Array<String> = context.resources.getStringArray(R.array.data_reimburse_array)
        when(transDetail.Transaction_ReimburseStatus){
            0,1 -> {
                transDetail.Transaction_ReimburseStatus++
                return array[transDetail.Transaction_ReimburseStatus]
            }
            else -> {
                transDetail.Transaction_ReimburseStatus = 0
                return array[0]
            }
        }
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
    var currentTyID: Int = 1

    fun setID(tyID: Int): CurrentTransactionType {
        val cTT = CurrentTransactionType()

        when (tyID) {
            1 -> {
                cTT.expense = R.color.app_title_text
                cTT.expensePointer = View.VISIBLE
            }
            2 -> {
                cTT.income = R.color.app_title_text
                cTT.incomePointer = View.VISIBLE
            }
            3 -> {
                cTT.transfer = R.color.app_title_text
                cTT.transferPointer = View.VISIBLE
            }
            4 -> {
                cTT.debitCredit = R.color.app_title_text
                cTT.debitCreditPointer = View.VISIBLE
            }
        }

        cTT.currentTyID = tyID

        return cTT
    }


}