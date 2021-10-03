package com.example.ngiu.ui.account

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Person

class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    // created getAllPerson
    fun readData(
        activity: FragmentActivity?
    ):List<Person> {
        val appDatabase = AppDatabase.getDatabase(activity!!)
        val personData= appDatabase.person().getAllPerson()
        return personData
    }

}