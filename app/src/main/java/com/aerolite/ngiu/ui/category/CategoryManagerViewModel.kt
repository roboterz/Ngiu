package com.aerolite.ngiu.ui.category

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aerolite.ngiu.functions.CATEGORY_LIMIT
import com.aerolite.ngiu.functions.CATEGORY_MAIN_INCOME
import com.aerolite.ngiu.functions.EDIT_MODE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_EXPENSE
import com.aerolite.ngiu.functions.TRANSACTION_TYPE_INCOME
import com.aerolite.ngiu.R
import com.aerolite.ngiu.data.AppDatabase
import com.aerolite.ngiu.data.entities.Category


class CategoryManagerViewModel: ViewModel() {

    var subCategory: MutableList<Category> = ArrayList()

    var mainCategory: MutableList<Category> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L
    var currentTransactionType: Long = 0L


    fun loadMainCategory(context: Context, mode: Int, transactionType: Long = TRANSACTION_TYPE_EXPENSE){

        //commonCategory = AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(categoryType)

        // Get Category which ParentID = 0L
        if (mode == EDIT_MODE){
            mainCategory = AppDatabase.getDatabase(context).category().getCategoryByTransactionTypeAndParentIDWithLimit(transactionType,0L, CATEGORY_LIMIT)
        }else{
            mainCategory = AppDatabase.getDatabase(context).category().getCategoryByTransactionTypeAndParentIDWithLimit(transactionType, 0L, CATEGORY_LIMIT -3)
        }

        // add Common section
        mainCategory.add(0, Category(0L, transactionType, 0L, context.getString(R.string.category_common)))

        when (transactionType){
            TRANSACTION_TYPE_EXPENSE -> {
                // add "+Add" item
                mainCategory.add(Category(0L,transactionType,0L, context.getString(R.string.menu_add_cate)))
            }
            TRANSACTION_TYPE_INCOME -> {
                // add Common section
                mainCategory.add(Category(CATEGORY_MAIN_INCOME, transactionType, 0L, context.getString(R.string.category_income)))
            }
        }


    }

    fun getSubCategory(context: Context ,mainCategoryID: Long, mode: Int, limit: Long = CATEGORY_LIMIT): MutableList<Category>{

        subCategory = if (mainCategoryID == 0L){
                        // common category
                        AppDatabase.getDatabase(context).category().getCommonCategoryByTransactionType(mainCategory[0].TransactionType_ID)
                    } else {
                        if (mode == EDIT_MODE){
                            AppDatabase.getDatabase(context).category().getCategoryByParentIDWithLimit(mainCategoryID, limit)
                        }else {
                            AppDatabase.getDatabase(context).category().getCategoryByParentID(mainCategoryID)
                        }
                    }

        // add "+Add" item
        if (mainCategoryID > 0L) {
            subCategory.add(
                Category(0L, TRANSACTION_TYPE_EXPENSE, 0L, context.getString(R.string.menu_add_cate), false)
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