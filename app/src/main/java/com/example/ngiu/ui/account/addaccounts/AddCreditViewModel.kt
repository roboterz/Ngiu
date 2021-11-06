package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account

class AddCreditViewModel : ViewModel() {


    fun insertCredit(context: Context, creditAccount: Account) {
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.account().addAccount(creditAccount)

    }


}