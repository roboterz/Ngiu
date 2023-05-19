package com.example.ngiu.ui.account.detail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.functions.*
import com.example.ngiu.ui.account.model.AccountCreditDetailGroupModel
import java.time.LocalDate
import java.time.LocalDateTime


class AccountCreditDetailViewModel : ViewModel() {

    private var listDetail: List<RecordDetail> = ArrayList()
    var accountRecord: Account = Account()
    var currentArrears: Double = 0.00
    var availableCreditLimit: Double = 0.00
    var listGroup: MutableList<AccountCreditDetailGroupModel> =ArrayList()


    fun loadDataToRam(context: Context, accountID: Long){

        // account
        accountRecord = AppDatabase.getDatabase(context).account().getRecordByAccountID(accountID)

        // Transactions
        listDetail = AppDatabase.getDatabase(context).trans().getTransRecordDetailByAccount(accountID)

        //val initialBalance = AppDatabase.getDatabase(context).account().getAccountInitialBalance(accountID)
        val totalAmountOfIncome = AppDatabase.getDatabase(context).trans().getTotalAmountOfIncomeByAccount(accountID)
        val totalAmountOfExpense = AppDatabase.getDatabase(context).trans().getTotalAmountOfExpenseByAccount(accountID)
        val totalAmountOfTransferOut = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferOutByAccount(accountID)
        val totalAmountOfTransferIn = AppDatabase.getDatabase(context).trans().getTotalAmountOfTransferInByAccount(accountID)

        // Arrears
        currentArrears = (accountRecord.Account_Balance + totalAmountOfExpense + totalAmountOfTransferOut - totalAmountOfIncome - totalAmountOfTransferIn)

        // Available Credit Limit
        availableCreditLimit = accountRecord.Account_CreditLimit - currentArrears

        // Group item
        var accountCDGM = AccountCreditDetailGroupModel(StatementStatus = true)

        // reset listGroup
        listGroup.clear()

        // if No record, add current statement term
        if (listDetail.isEmpty()){
            // todo add current statement term
            accountCDGM.TermEndDate = LocalDateTime.of(LocalDate.now().year, LocalDate.now().monthValue, accountRecord.Account_StatementDay, 0,0,0)
            accountCDGM.TermStartDate = accountCDGM.TermEndDate.minusMonths(1)
            accountCDGM.CDList.reverse()
            accountCDGM.DueAmount =  0.00 + if (listGroup.isNotEmpty()) listGroup.last().DueAmount else 0.00
            accountCDGM.StatementStatus = false
            listGroup.add(accountCDGM.copy())

        }else{
            // if has records

            // Term Start Date
            var startDate = LocalDateTime.of(listDetail.last().Transaction_Date.year, listDetail.last().Transaction_Date.monthValue,  accountRecord.Account_StatementDay, 0,0,0)
            if (listDetail.last().Transaction_Date.dayOfMonth < accountRecord.Account_StatementDay){
                startDate = startDate.minusMonths(1)
            }
            // Term End Date
            var endDate = startDate.plusMonths(1)

            var sumAmount: Double = 0.00


            for (i in listDetail.indices.reversed()){
                if (listDetail[i].Transaction_Date < endDate && listDetail[i].Transaction_Date >= startDate){
                    // the date in the current term, add
                    accountCDGM.CDList.add(listDetail[i].copy())

                    when (listDetail[i].TransactionType_ID){
                        TRANSACTION_TYPE_EXPENSE -> {
                            sumAmount += listDetail[i].Transaction_Amount
                        }
                        TRANSACTION_TYPE_INCOME -> {
                            sumAmount -= listDetail[i].Transaction_Amount
                        }
                        TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT -> {
                            if (listDetail[i].Account_ID == accountID)
                                sumAmount += listDetail[i].Transaction_Amount
                            else
                                sumAmount -= listDetail[i].Transaction_Amount
                        }
                    }

                }else{
                    // the date is Not in the term, change the end date and start date.

                    // add new group item
                    accountCDGM.TermEndDate = endDate
                    accountCDGM.TermStartDate = startDate
                    accountCDGM.CDList.reverse()
                    accountCDGM.DueAmount = sumAmount + if (listGroup.isNotEmpty()) listGroup.last().DueAmount else 0.00
                    listGroup.add(accountCDGM.copy())
                    // reset value
                    accountCDGM = AccountCreditDetailGroupModel(StatementStatus = true)
                    sumAmount = 0.00

                    //
                    while ( endDate < listDetail[i].Transaction_Date  ){
                        // next term
                        endDate = endDate.plusMonths(1)
                        startDate = startDate.plusMonths(1)

                        if (listDetail[i].Transaction_Date < endDate && listDetail[i].Transaction_Date >= startDate) {
                            // the date in the current term, add
                            accountCDGM.CDList.add(listDetail[i].copy())
                            //
                            when (listDetail[i].TransactionType_ID) {
                                TRANSACTION_TYPE_EXPENSE -> {
                                    sumAmount += listDetail[i].Transaction_Amount
                                }

                                TRANSACTION_TYPE_INCOME -> {
                                    sumAmount -= listDetail[i].Transaction_Amount
                                }

                                TRANSACTION_TYPE_TRANSFER, TRANSACTION_TYPE_DEBIT -> {
                                    if (listDetail[i].Account_ID == accountID)
                                        sumAmount += listDetail[i].Transaction_Amount
                                    else
                                        sumAmount -= listDetail[i].Transaction_Amount
                                }
                            }

                        }else{
                            // add new group item
                            accountCDGM.TermEndDate = endDate
                            accountCDGM.TermStartDate = startDate
                            accountCDGM.CDList.reverse()
                            accountCDGM.DueAmount = sumAmount + if (listGroup.isNotEmpty()) listGroup.last().DueAmount else 0.00
                            listGroup.add(accountCDGM.copy())
                            // reset value
                            accountCDGM = AccountCreditDetailGroupModel(StatementStatus = true)
                            sumAmount = 0.00
                        }

                    }


                }

            }

            //
            if (accountCDGM.CDList.isNotEmpty()) {
                // add new group item
                accountCDGM.TermEndDate = endDate
                accountCDGM.TermStartDate = startDate
                accountCDGM.CDList.reverse()
                accountCDGM.DueAmount = sumAmount + if (listGroup.isNotEmpty()) listGroup.last().DueAmount else 0.00
                accountCDGM.StatementStatus = false
                listGroup.add(accountCDGM.copy())
            }

        }

        //
        listGroup.reverse()

    }



}