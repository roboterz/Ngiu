package com.example.ngiu.data.entities.returntype

import androidx.room.PrimaryKey

data class RecordSubCategory (
    val SubCategory_ID: Long = 0,
    val MainCategory_ID: Long = 0,
    val TransactionType_ID: Long = 0,
    val SubCategory_Name: String = "",
    val SubCategory_Common: Boolean = false
)