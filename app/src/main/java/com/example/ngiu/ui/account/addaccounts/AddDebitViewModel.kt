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

}