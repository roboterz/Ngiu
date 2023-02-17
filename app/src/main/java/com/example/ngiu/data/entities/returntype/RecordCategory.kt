package com.example.ngiu.data.entities.returntype

import androidx.room.PrimaryKey

data class RecordCategory (
    var Category_ID: Long = 0,
    var TransactionType_ID: Long = 0,
    var Category_ParentID: Long = 0,
    var Category_Name: String = "",
    var Category_Common: Boolean = false
)