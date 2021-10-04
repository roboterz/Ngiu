package com.example.ngiu.ui.activity

import android.widget.Toast
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Person
import com.example.ngiu.data.entities.Trans
import kotlinx.android.synthetic.main.transaction_cardview.*

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
    ):List<Trans> {
        val appDatabase = AppDatabase.getDatabase(activity!!)
        val TransData= appDatabase.trans().getAllTrans()

        return TransData
    }

}