package com.example.ngiu.ui.category

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory
import com.example.ngiu.data.entities.returntype.RecordSubCategory
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.ui.activity.ActivityFragment

class CategoryManagerViewModel: ViewModel() {

    private var subCategory: List<RecordSubCategory> = ArrayList()

    var mainCategory: List<MainCategory> = ArrayList()
    var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L


    fun loadDataToRam(context: Context ,categoryType: Long){

        commonCategory = AppDatabase.getDatabase(context).subcat().getCommonCategoryByTransactionType(categoryType)

        mainCategory = AppDatabase.getDatabase(context).mainCategory().getMainCategoryByTransactionType(categoryType)

        //subCategory = AppDatabase.getDatabase(context).subcat().getAllSubCategory()
    }

    fun getSubCategory(context: Context ,mainCategoryID: Long): List<SubCategory>{
        val subCateList: ArrayList<SubCategory> = ArrayList()

        return if (mainCategoryID == 0L){
                    // common category
                    commonCategory
                } else {
                    AppDatabase.getDatabase(context).subcat().getSubCategoryByMainCategoryID(mainCategoryID)
                }

    }


}