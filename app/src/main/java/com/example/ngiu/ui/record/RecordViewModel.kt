package com.example.ngiu.ui.record


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
    var transDetail = TransactionDetail(0, 0, "", "", "", 0.00, Date(), "", "", "", "", 0)

    var subCategoryName = ArrayList<String>()

    var expenseCommonCategory = ArrayList<SubCategory>()
    var incomeCommonCategory = ArrayList<SubCategory>()
    var transferCommonCategory = ArrayList<SubCategory>()
    var debitCreditCommonCategory = ArrayList<SubCategory>()
    var subCategory: List<RecordSubCategory> = ArrayList()
    var person: List<Person> = ArrayList()
    var merchant: List<Merchant> = ArrayList()
    var account: List<Account> = ArrayList()
    var project: List<Project> = ArrayList()


    fun setTransactionType(tyID: Int): CurrentTransactionType {
        currentTransactionType = currentTransactionType.setID(tyID)
        return currentTransactionType
    }


    fun readCommonCategory(activity: FragmentActivity?, tyID: Int): List<SubCategory> {

        return when (tyID) {
            1 -> AppDatabase.getDatabase(activity!!).subcat().getExpenseCommonCategory()
            2 -> AppDatabase.getDatabase(activity!!).subcat().getIncomeCommonCategory()
            3 -> AppDatabase.getDatabase(activity!!).subcat().getTransferCommonCategory()
            4 -> AppDatabase.getDatabase(activity!!).subcat().getDebitCreditCommonCategory()
            else -> ArrayList<SubCategory>()
        }

    }

    //
    fun loadDataToRam(activity: FragmentActivity?) {
        val database = AppDatabase.getDatabase(activity!!)
        subCategory = database.subcat().getAllSubCategory()
        account = database.account().getAllAccount()
        for (i in subCategory.indices) {
            when (subCategory[i].TransactionType_ID) {
                1L -> expenseCommonCategory.add(
                    SubCategory(
                        subCategory[i].SubCategory_ID,
                        subCategory[i].MainCategory_ID,
                        subCategory[i].SubCategory_Name,
                        subCategory[i].SubCategory_Common
                    )
                )
                2L -> incomeCommonCategory.add(
                    SubCategory(
                        subCategory[i].SubCategory_ID,
                        subCategory[i].MainCategory_ID,
                        subCategory[i].SubCategory_Name,
                        subCategory[i].SubCategory_Common
                    )
                )
                3L -> transferCommonCategory.add(
                    SubCategory(
                        subCategory[i].SubCategory_ID,
                        subCategory[i].MainCategory_ID,
                        subCategory[i].SubCategory_Name,
                        subCategory[i].SubCategory_Common
                    )
                )
                4L -> debitCreditCommonCategory.add(
                    SubCategory(
                        subCategory[i].SubCategory_ID,
                        subCategory[i].MainCategory_ID,
                        subCategory[i].SubCategory_Name,
                        subCategory[i].SubCategory_Common
                    )
                )
            }
        }
        subCategoryName.add(expenseCommonCategory[0].SubCategory_Name)
        subCategoryName.add(incomeCommonCategory[0].SubCategory_Name)
        subCategoryName.add(transferCommonCategory[0].SubCategory_Name)
        subCategoryName.add(debitCreditCommonCategory[0].SubCategory_Name)
    }

    fun getOneSubCategory(activity: FragmentActivity?, rID: Long): SubCategory {
        return AppDatabase.getDatabase(activity!!).subcat().getRecordByID(rID)
    }

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