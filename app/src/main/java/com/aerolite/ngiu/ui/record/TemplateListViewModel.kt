package com.aerolite.ngiu.ui.record

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Template
import com.aerolite.ngiu.data.entities.returntype.TemplateDetail
import com.aerolite.ngiu.data.entities.returntype.TransactionDetail

class TemplateListViewModel(application: Application): AndroidViewModel(application) {


    private val myDatabase = AppDatabase.getDatabase(application).template()


    fun getAllRecords(): LiveData<List<TemplateDetail>> {

        return myDatabase.getAllTemplateDetail()
    }

    fun getOneRecord(templateID: Long): TemplateDetail {

        return myDatabase.getOneTemplateDetailByID(templateID)
    }
    
}