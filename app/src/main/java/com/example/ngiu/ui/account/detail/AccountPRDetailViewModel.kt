package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.functions.CATEGORY_SUB_BORROW
import com.example.ngiu.functions.CATEGORY_SUB_LEND
import com.example.ngiu.functions.CATEGORY_SUB_PAYMENT
import com.example.ngiu.functions.CATEGORY_SUB_RECEIVE_PAYMENT


class AccountPRDetailViewModel : ViewModel() {

    var listPRDetail: List<RecordDetail> = ArrayList()
    var accountBalance: Double = 0.00
    var lendAmount: Double = 0.00
    var receiveAmount: Double = 0.00
    var borrowAmount: Double = 0.00
    var payAmount: Double = 0.00
    var accountID: Long = 0L
    var accountName: String =""
    var accountTypeID: Long = 0L

    fun loadDataToRam(context: Context){
        listPRDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(accountID)

        lendAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYCategoryID(accountID, CATEGORY_SUB_LEND)
        receiveAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYCategoryID(accountID, CATEGORY_SUB_RECEIVE_PAYMENT)
        borrowAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYCategoryID(accountID, CATEGORY_SUB_BORROW)
        payAmount = AppDatabase.getDatabase(context).trans().getTotalAmountFromPRAccountBYCategoryID(accountID, CATEGORY_SUB_PAYMENT)

        accountBalance = lendAmount + payAmount - receiveAmount - borrowAmount
    }
}