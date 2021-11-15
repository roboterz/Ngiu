package com.example.ngiu.ui.activity

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Trans
import com.example.ngiu.data.entities.returntype.TransactionDetail

class ActivityViewModel : ViewModel() {


    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    */

    private var transactionDetail: List<TransactionDetail> = ArrayList()


    fun getRecordByID(activity: FragmentActivity?, rID: Long): Trans {
        return AppDatabase.getDatabase(activity!!).trans().getRecordByID(rID)
    }

    fun loadDataToRam(activity: FragmentActivity?){
        transactionDetail = AppDatabase.getDatabase(activity!!).trans().getAllTrans()
    }

    fun getData():List<TransactionDetail>{
        return transactionDetail
    }

}