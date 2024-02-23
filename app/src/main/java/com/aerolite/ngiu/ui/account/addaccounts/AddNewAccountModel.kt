package com.aerolite.ngiu.ui.account.addaccounts

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency

class AddNewAccountModel(application: Application) : AndroidViewModel(application) {

    val appDatabase = AppDatabase.getDatabase(application)

    fun insertCash(acct: Account) {
        appDatabase.account().addAccount(acct)

    }

    fun getCurrency(): List<Currency> {
        return appDatabase.currency().getAllCurrency()
    }

    fun getAccountByID(acctID: Long): Account {
        return appDatabase.account().getRecordByAccountID(acctID)
    }

    fun updateAccount(account: Account){
        appDatabase.account().updateAccount(account)
    }

    fun getAllAccount(): List<Account> {
        return appDatabase.account().getAllAccount()
    }

    fun deleteAccount(acctID: Long){

        val acct = Account(Account_ID = acctID)

        appDatabase.account().deleteAccount(acct)
    }


}