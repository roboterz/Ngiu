package com.example.ngiu.ui.calendar

import android.text.Editable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Person

class CalendarViewModel : ViewModel() {

    var recordId: Long = 0
    private val _text = MutableLiveData<String>().apply {
        value = "Record info:"
    }
    val text: LiveData<String> = _text


    fun insertData(
        activity: FragmentActivity?,
        strName: Editable,
        strDate: Editable,
        strMemo: Editable
    ): Long {

            val person = Person(Person_ID= 0, Person_Name= strName.toString())
        val appDatabase = AppDatabase.getDatabase(activity!!)
            recordId= appDatabase.person().addPerson(person)
        return recordId
    }

}