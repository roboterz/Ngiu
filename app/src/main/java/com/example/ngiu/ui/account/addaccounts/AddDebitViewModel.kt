package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency

class AddDebitViewModel : ViewModel() {


    fun insertDebit(context: Context, debitAccount: Account) {
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.account().addAccount(debitAccount)

    }

    fun getCurrency(context: Context): List<Currency> {
        val appDatabase = AppDatabase.getDatabase(context)
        return appDatabase.currency().getAllCurrency()
    }

    fun getAccountByID(context: Context, id: Long): Account {
        val appDatabase = AppDatabase.getDatabase(context)
        return appDatabase.account().getRecordByID(id)
    }

    fun updateAccount(context: Context, account: Account){
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.account().updateAccount(account)
    }

    fun getAllAccount(context: Context): List<Account> {
        val appDatabase = AppDatabase.getDatabase(context)
        return appDatabase.account().getAllAccount()
    }


}