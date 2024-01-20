package com.aerolite.ngiu.ui.account.addaccounts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.returntype.AccountTypeUIModel


class AddAccountViewModel : ViewModel() {
    var accountType = MutableLiveData<List<AccountTypeUIModel>>()

     fun getAccountType(context: Context) {
         val appDatabase = AppDatabase.getDatabase(context)
         val accountTypes = appDatabase.accountType().getAllAccountType()

         val sections = ArrayList<AccountTypeUIModel>()

         accountTypes.forEach { item ->
             val sectionModel = AccountTypeUIModel(item.AccountType_Name, item.AccountType_Memo)
             sections.add(sectionModel)

         }
     accountType.value = sections;

    }

}