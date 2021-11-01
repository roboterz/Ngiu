package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Account

class AddCashViewModel : ViewModel() {


    fun insertData(context: Context, account: Account) {
        //save data to database
        val appDatabase = AppDatabase.getDatabase(context)
        appDatabase.account().addAccount(account)

    }


}