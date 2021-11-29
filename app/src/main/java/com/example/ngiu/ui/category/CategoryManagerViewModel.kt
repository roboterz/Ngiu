package com.example.ngiu.ui.category

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory


class CategoryManagerViewModel: ViewModel() {

    var subCategory: MutableList<SubCategory> = ArrayList()

    var mainCategory: MutableList<MainCategory> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L
    var currentTransactionType: Long = 0L


    fun loadMainCategory(context: Context ,transactionType: Long){

        //commonCategory = AppDatabase.getDatabase(context).subcat().getCommonCategoryByTransactionType(categoryType)

        mainCategory = AppDatabase.getDatabase(context).mainCategory().getMainCategoryByTransactionType(transactionType)


        if (transactionType == 1L){
            // add Common section
            mainCategory[0].MainCategory_ID = 0L
            mainCategory[0].MainCategory_Name = context.getString(R.string.option_category_common)
            // add "+Add" item
            mainCategory.add(MainCategory(0L,0L, context.getString(R.string.menu_add_cate)))
        }else{
            // add Common section
            mainCategory.add(0, MainCategory(0L, transactionType, context.getString(R.string.option_category_common)))
        }


    }

    fun getSubCategory(context: Context ,mainCategoryID: Long): MutableList<SubCategory>{

        subCategory = if (mainCategoryID == 0L){
                        // common category
                        AppDatabase.getDatabase(context).subcat().getCommonCategoryByTransactionType(mainCategory[0].TransactionType_ID)
                    } else {
                        AppDatabase.getDatabase(context).subcat().getSubCategoryByMainCategoryID(mainCategoryID)
                    }


        // add "+Add" item
        if (mainCategoryID > 0L) {
            subCategory.add(
                SubCategory(0L, mainCategoryID, context.getString(R.string.menu_add_cate), false)
            )

            if (mainCategory[0].TransactionType_ID == 2L) subCategory.removeAt(0)
        }
        return subCategory
    }

    fun addMainCategory(mCategory: MainCategory) {
        mainCategory.add(mCategory)
    }



}