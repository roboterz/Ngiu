package com.example.ngiu.functions.chart

data class CategoryAmount(
    var Category_ID: Long = 0L,
    var Category_Name: String = "",
    var Amount: Double = 0.0,
    var Category_ParentID: Long = 0L,
    var TransactionType_ID: Long = 0L

)
