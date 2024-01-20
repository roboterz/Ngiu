package com.aerolite.ngiu.ui.reimburse

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.data.entities.returntype.TransactionDetail
import com.aerolite.ngiu.functions.ACCOUNT_TYPE_RECEIVABLE
import com.aerolite.ngiu.functions.CATEGORY_INCOME_REIMBURSE
import com.aerolite.ngiu.functions.REIMBURSABLE
import com.aerolite.ngiu.functions.REIMBURSED
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import java.time.format.DateTimeFormatter


class ReimburseViewModel: ViewModel(){

    private var listDetail: List<TransactionDetail> = ArrayList()
    private var listAccounts: List<Account> = ArrayList()
    private var idxAccount: Int = 0
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    var accountBalance: Double = 0.00
//    var inflowAmount: Double = 0.00
//    var outflowAmount: Double = 0.00
//    var accountID: Long = 0L
//    var accountName: String =""
//    var accountTypeID: Long = 0L
//
    fun loadDataToRam(context: Context){

        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByReimburseStatus(
            REIMBURSABLE
        )
        listAccounts = AppDatabase.getDatabase(context).account().getAccountExceptType(
            ACCOUNT_TYPE_RECEIVABLE
        )

    }


    fun getListOfAccountName(): Array<String> {
        val nameList: ArrayList<String> = ArrayList()

        for (at in listAccounts) {
            nameList.add(at.Account_Name)
        }

        return nameList.toTypedArray()
    }


    fun getFirstAccountName(): String{
        idxAccount = 0
        return listAccounts[0].Account_Name
    }


    fun getSumOfReimbursed(): Double{
        var sum = 0.00

        for (i in listDetail){
            if (i.Transaction_ReimburseStatus == REIMBURSED){
                sum += i.Transaction_Amount
            }
        }

        return sum
    }


    fun getCountOfReimbursed(): Int{
        var count = 0

        for (i in listDetail){
            if (i.Transaction_ReimburseStatus == REIMBURSED){
                count ++
            }
        }

        return count
    }


    fun getListDetail(): List<TransactionDetail>{
        return listDetail
    }

    fun setAccountIdx(idx: Int){
        idxAccount = idx
    }

    fun setReimburseStatus(transID: Long, selected: Boolean){
        for (i in listDetail.indices){
            if (listDetail[i].Transaction_ID == transID){
                listDetail[i].Transaction_ReimburseStatus = if (selected) REIMBURSED else REIMBURSABLE
            }
        }
    }

    fun setAllReimburseStatus(selected: Boolean){
        for (i in listDetail.indices){
            listDetail[i].Transaction_ReimburseStatus = if (selected) REIMBURSED else REIMBURSABLE
        }
    }

    fun saveListDetail(context: Context, totalAmount: Double, claimAmount: Double, countTrans: Int = 1){
        val tempList: MutableList<Trans> = ArrayList()
        var tempCateName =""

        for (i in listDetail.indices){
            if (listDetail[i].Transaction_ReimburseStatus == REIMBURSED){
                tempList.add(
                    Trans(
                    Transaction_ID = listDetail[i].Transaction_ID,
                    TransactionType_ID = listDetail[i].TransactionType_ID,
                    Category_ID = listDetail[i].Category_ID ,
                    Account_ID = listDetail[i].Account_ID ,
                    AccountRecipient_ID = listDetail[i].AccountRecipient_ID ,
                    Transaction_Amount = listDetail[i].Transaction_Amount ,
                    Transaction_Amount2 = listDetail[i].Transaction_Amount2 ,
                    Transaction_Date = listDetail[i].Transaction_Date ,
                    Person_ID = listDetail[i].Person_ID ,
                    Merchant_ID = listDetail[i].Merchant_ID ,
                    Transaction_Memo = listDetail[i].Transaction_Memo ,
                    Project_ID = listDetail[i].Project_ID ,
                    Transaction_ReimburseStatus = listDetail[i].Transaction_ReimburseStatus ,
                    Period_ID = listDetail[i].Period_ID ,
                    Transaction_UploadStatus = false,
                    Transaction_IsDelete = false
                )
                )

                if (tempCateName == "") {
                    tempCateName = listDetail[i].Category_Name
                }
            }
        }

        // todo create a new record for reimburse
        var newTrans = Trans(
            TransactionType_ID = TRANSACTION_TYPE_INCOME,
            Category_ID = CATEGORY_INCOME_REIMBURSE,
            Account_ID = listAccounts[idxAccount].Account_ID ,
            AccountRecipient_ID = listAccounts[idxAccount].Account_ID,
            Transaction_Amount = totalAmount ,
            Transaction_Amount2 = 0.0 ,
            Transaction_Memo =
                context.getString(R.string.msg_total) + " " + countTrans.toString() + " " +
                context.getString(R.string.msg_transaction) + " " + context.getString(R.string.msg_from) + " " +
                tempList[0].Transaction_Date.format(dateFormatter) + " " + tempCateName + ". " +
                context.getString(R.string.msg_total) + " $" + "%.2f".format(totalAmount) + ". " +
                context.getString(R.string.msg_claimed) + " $" + "%.2f".format(claimAmount) + "."
        )

        // add new transaction
        AppDatabase.getDatabase(context).trans().addTransaction(newTrans)

        // update reimburse status
        AppDatabase.getDatabase(context).trans().updateTransaction(*tempList.toTypedArray())
    }

}