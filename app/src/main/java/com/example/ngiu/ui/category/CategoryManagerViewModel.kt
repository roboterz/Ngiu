package com.example.ngiu.ui.category

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory


class CategoryManagerViewModel: ViewModel() {

    var subCategory: List<SubCategory> = ArrayList()

    var mainCategory: MutableList<MainCategory> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L
    var currentTransactionType: Long = 0L


    fun loadMainCategory(context: Context ,transactionType: Long){

        //commonCategory = AppDatabase.getDatabase(context).subcat().getCommonCategoryByTransactionType(categoryType)

        mainCategory = AppDatabase.getDatabase(context).mainCategory().getMainCategoryByTransactionType(transactionType)

        mainCategory.add(0, MainCategory(0L, transactionType, context.getString(R.string.option_category_common)))

        //subCategory = AppDatabase.getDatabase(context).subcat().getAllSubCategory()
    }

    fun getSubCategory(context: Context ,mainCategoryID: Long): List<SubCategory>{

        subCategory = if (mainCategoryID == 0L){
                        // common category
                        AppDatabase.getDatabase(context).subcat().getCommonCategoryByTransactionType(mainCategory[0].TransactionType_ID)
                    } else {
                        AppDatabase.getDatabase(context).subcat().getSubCategoryByMainCategoryID(mainCategoryID)
                    }

        return subCategory
    }

    fun addMainCategory(mCategory: MainCategory) {
        mainCategory.add(mCategory)
    }



}