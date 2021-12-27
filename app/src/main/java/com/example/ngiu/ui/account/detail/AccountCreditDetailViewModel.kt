package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.ui.account.model.AccountCreditDetailGroupModel
import java.time.LocalDate
import java.time.LocalDateTime


class AccountCreditDetailViewModel : ViewModel() {

    var listDetail: List<RecordDetail> = ArrayList()
    var accountRecord: Account = Account()
    var currentArrears: Double = 0.00
    var availableCreditLimit: Double = 0.00
    var listGroup: MutableList<AccountCreditDetailGroupModel> =ArrayList()


    fun loadDataToRam(context: Context, accountID: Long){

        accountRecord = AppDatabase.getDatabase(context).account().getRecordByID(accountID)

        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(accountID)

        val initialBalance = AppDatabase.getDatabase(context).account().getAccountInitialBalance(accountID)
        val totalAmountOfIncome = AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(accountID)
        val totalAmountOfExpense = AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(accountID)
        val totalAmountOfTransferOut = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(accountID)
        val totalAmountOfTransferIn = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(accountID)

        currentArrears = (initialBalance + totalAmountOfIncome + totalAmountOfTransferIn - totalAmountOfExpense - totalAmountOfTransferOut) * -1

        availableCreditLimit = accountRecord.Account_CreditLimit - currentArrears


        var amt: Double = 0.00
        var addRecordFlag = true
        var accountCDGM = AccountCreditDetailGroupModel()
        // end date
        var endDate = LocalDateTime.of(LocalDate.now().year, LocalDate.now().monthValue, accountRecord.Account_StatementDay, 23,59,59)
        //var endDate = LocalDateTime.of(LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, accountRecord.Account_StatementDay), LocalTime.MIDNIGHT)
        if (LocalDate.now().dayOfMonth > accountRecord.Account_StatementDay) {
            endDate = endDate.plusMonths(1)
        }
        // start date
        var startDate = endDate.minusMonths(1).plusDays(1).withHour(0).withMinute(0).withSecond(0)

        listGroup.clear()

        for (i in listDetail.indices){
            //if (listDetail[i].Transaction_Date.isBefore(endDate) && listDetail[i].Transaction_Date.isAfter(startDate)){
            if (listDetail[i].Transaction_Date < endDate && listDetail[i].Transaction_Date >= startDate){
                // in this term
                accountCDGM.CDList.add(listDetail[i].copy())
                when (listDetail[i].TransactionType_ID){
                    1L -> {amt += listDetail[i].Transaction_Amount}
                    2L -> {amt -= listDetail[i].Transaction_Amount}
                    3L, 4L -> {
                        if (listDetail[i].Account_ID == accountID) amt += listDetail[i].Transaction_Amount
                        else amt -= listDetail[i].Transaction_Amount
                    }
                }
                addRecordFlag = true
            }else{
                // next term
                accountCDGM.TermEndDate = endDate
                accountCDGM.TermStartDate = startDate
                accountCDGM.DueAmount = amt
                listGroup.add(accountCDGM.copy())
                // reset
                accountCDGM = AccountCreditDetailGroupModel()
                amt = 0.00
                addRecordFlag = false
                accountCDGM.StatementStatus = true


                while(!addRecordFlag){
                    endDate = endDate.minusMonths(1)
                    startDate = startDate.minusMonths(1)
                    if (listDetail[i].Transaction_Date < endDate && listDetail[i].Transaction_Date >= startDate){
                        accountCDGM.CDList.add(listDetail[i].copy())
                        when (listDetail[i].TransactionType_ID){
                            1L -> {amt += listDetail[i].Transaction_Amount}
                            2L -> {amt -= listDetail[i].Transaction_Amount}
                            3L, 4L -> {
                                if (listDetail[i].Account_ID == accountID) amt += listDetail[i].Transaction_Amount
                                else amt -= listDetail[i].Transaction_Amount
                            }
                        }
                        addRecordFlag = true
                    }
                    if (!addRecordFlag){
                        accountCDGM.TermEndDate = endDate
                        accountCDGM.TermStartDate = startDate
                        accountCDGM.DueAmount = amt
                        listGroup.add(accountCDGM.copy())
                        // reset
                        accountCDGM = AccountCreditDetailGroupModel()
                        amt = 0.00
                        accountCDGM.StatementStatus = true
                    }
                }
            }
        }

        if (addRecordFlag){
            accountCDGM.TermEndDate = endDate
            accountCDGM.TermStartDate = startDate
            accountCDGM.DueAmount = amt
            listGroup.add(accountCDGM.copy())
        }
    }
}