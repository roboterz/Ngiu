package com.example.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.ui.account.model.AccountSectionUiModel
import com.example.ngiu.ui.account.model.AccountTypeUIModel
import com.google.android.gms.common.internal.AccountType


class AddAccountViewModel : ViewModel() {
    var accountType = MutableLiveData<List<AccountTypeUIModel>>()

     fun getAccountType(context: Context) {
         val appDatabase = AppDatabase.getDatabase(context)
         val accountTypes = appDatabase.accounttype().getAllAccountType()

         val sections = ArrayList<AccountTypeUIModel>()

         accountTypes.forEach { item ->
             val sectionModel = AccountTypeUIModel(item.AccountType_Name, item.AccountType_Memo)
             sections.add(sectionModel)

         }
     accountType.value = sections;

    }

}