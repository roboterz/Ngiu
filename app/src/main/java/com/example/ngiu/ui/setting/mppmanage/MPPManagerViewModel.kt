package com.example.ngiu.ui.setting.mppmanage

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Category
import com.example.ngiu.data.entities.returntype.MPPItem
import com.example.ngiu.functions.MPP_MERCHANT
import com.example.ngiu.functions.MPP_PERSON
import com.example.ngiu.functions.MPP_PROJECT


class MPPManagerViewModel: ViewModel() {

    var itemList: MutableList<MPPItem> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var typeID: Int = 0


    fun loadData(context: Context ,type: Int){

        itemList.clear()
        when (type) {
            // merchant
            MPP_MERCHANT -> {
                val mc = AppDatabase.getDatabase(context).merchant().getAllMerchant()
                for ( i in mc){
                    itemList.add(MPPItem(i.Merchant_ID, i.Merchant_Name))
                }
            }
            // person
            MPP_PERSON -> {
                val ps = AppDatabase.getDatabase(context).person().getAllPerson()
                for ( i in ps){
                    itemList.add(MPPItem(i.Person_ID, i.Person_Name))
                }
            }
            // project
            MPP_PROJECT -> {
                val pj = AppDatabase.getDatabase(context).project().getAllProject()
                for ( i in pj){
                    itemList.add(MPPItem(i.Project_ID, i.Project_Name))
                }
            }

        }
        itemList.add(MPPItem(0,"+ Add"))

    }





}