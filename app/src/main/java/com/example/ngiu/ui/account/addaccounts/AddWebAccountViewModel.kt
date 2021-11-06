package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency

class AddWebAccountViewModel:ViewModel() {
    fun insertWebAccount(context: Context, webAccount: Account) {
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.account().addAccount(webAccount)

    }

    fun getCurrency(context: Context): List<Currency> {
        val appDatabase = AppDatabase.getDatabase(context)
        return appDatabase.currency().getAllCurrency()
    }
}