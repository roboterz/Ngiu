package com.example.ngiu.ui.record


import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.TransactionType
import com.example.ngiu.data.entities.returntype.TransactionDetail
import java.util.*
import kotlin.collections.ArrayList

class RecordViewModel : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

     */

    var currentTransactionType: CurrentTransactionType = CurrentTransactionType()

    var recordOpenOption : Int = 0

    var currentRowID: Long = 0

    var transRecord : Trans = Trans(0,0,0,0,0,0.0,
    Date(),0,0,"",0,0,0)

    fun setTransactionType(tyID: Int): CurrentTransactionType{

        currentTransactionType = currentTransactionType.setID(tyID)

        return currentTransactionType
    }


    fun readCommonCategory(activity: FragmentActivity?, tyID: Int): List<SubCategory>{

        return when (tyID){
            1 -> AppDatabase.getDatabase(activity!!).subcat().getExpenseCommonCategory()
            2 -> AppDatabase.getDatabase(activity!!).subcat().getIncomeCommonCategory()
            3 -> AppDatabase.getDatabase(activity!!).subcat().getTransferCommonCategory()
            4 -> AppDatabase.getDatabase(activity!!).subcat().getDebitCreditCommonCategory()
            else -> ArrayList<SubCategory>()
        }

    }

    //


    fun getOneSubCategory(activity: FragmentActivity?, rID: Long): SubCategory{
        return AppDatabase.getDatabase(activity!!).subcat().getRecordByID(rID)
    }

    fun getOneTrans(activity: FragmentActivity?, rID: Long): Trans{
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun getOneTransactionDetail(activity: FragmentActivity?, rID: Long): TransactionDetail{
        return AppDatabase.getDatabase(activity!!).trans().getOneTransaction(rID)
    }

}

class CurrentTransactionType{
    var expense:  Int = R.color.app_title_text_inactive
    var expensePointer: Int = View.INVISIBLE
    var income:  Int = R.color.app_title_text_inactive
    var incomePointer:  Int = View.INVISIBLE
    var transfer:  Int = R.color.app_title_text_inactive
    var transferPointer:  Int = View.INVISIBLE
    var debitCredit:  Int = R.color.app_title_text_inactive
    var debitCreditPointer:  Int = View.INVISIBLE
    var currentTyID: Int = 1

    fun setID(tyID: Int): CurrentTransactionType{
        val cTT = CurrentTransactionType()

        when (tyID){
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