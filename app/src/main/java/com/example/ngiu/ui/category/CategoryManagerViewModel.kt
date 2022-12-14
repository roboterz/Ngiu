package com.example.ngiu.ui.category

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ngiu.R
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.Category


class CategoryManagerViewModel: ViewModel() {

    var subCategory: MutableList<Category> = ArrayList()

    var mainCategory: MutableList<Category> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L
    var currentTransactionType: Long = 0L


    fun loadMainCategory(context: Context ,transactionType: Long){

        //commonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(categoryType)

        // Get Category which ParentID = 0L
        mainCategory = AppDatabase.getDatabase(context).category().getCategoryByTransactionTypeAndParentID(transactionType,0L)


        if (transactionType == 1L){
            // add Common section
            mainCategory[0].Category_ID = 0L
            mainCategory[0].Category_Name = context.getString(R.string.option_category_common)
            // add "+Add" item
            mainCategory.add(Category(0L,transactionType,0L, context.getString(R.string.menu_add_cate)))
        }else{
            // add Common section
            mainCategory.add(0, Category(0L, transactionType, 0L, context.getString(R.string.option_category_common)))
        }


    }

    fun getSubCategory(context: Context ,mainCategoryID: Long): MutableList<Category>{

        subCategory = if (mainCategoryID == 0L){
                        // common category
                        AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(mainCategory[0].TransactionType_ID)
                    } else {
                        AppDatabase.getDatabase(context).category().getCategoryByParentID(mainCategoryID)
                    }


        // add "+Add" item
        if (mainCategoryID > 0L) {
            subCategory.add(
                Category(0L, 1L, 0L, context.getString(R.string.menu_add_cate), false)
            )
        }
        return subCategory
    }

    fun addMainCategory(mCategory: Category) {
        mainCategory.add(mCategory)
    }


    fun updateCategory(context: Context, category: Category){
        AppDatabase.getDatabase(context).category().updateCategory(category)
    }


    fun addCategory(context: Context, category: Category){
        AppDatabase.getDatabase(context).category().addCategory(category)
    }

    fun deleteCategory(context: Context, category: Category){
        AppDatabase.getDatabase(context).category().deleteCategoryByParentID(category.Category_ID)
        AppDatabase.getDatabase(context).category().deleteCategory(category)
    }

}