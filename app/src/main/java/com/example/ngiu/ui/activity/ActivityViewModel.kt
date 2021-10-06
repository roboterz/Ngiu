package com.example.ngiu.ui.activity

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.list.TransactionDetail

class ActivityViewModel : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    */

    // created getAllPerson
    fun readData(
        activity: FragmentActivity?
    ): List<TransactionDetail> {
        val appDatabase = AppDatabase.getDatabase(activity!!)

        return appDatabase.trans().getAllTrans()
    }

}