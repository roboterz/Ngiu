package com.example.ngiu.data.entities.returntype

import androidx.room.PrimaryKey

data class RecordSubCategory (
    var SubCategory_ID: Long = 0,
    var MainCategory_ID: Long = 0,
    var TransactionType_ID: Long = 0,
    var SubCategory_Name: String = "",
    var SubCategory_Common: Boolean = false
)