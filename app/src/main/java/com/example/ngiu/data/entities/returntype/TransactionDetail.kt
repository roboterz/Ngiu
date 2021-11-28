package com.example.ngiu.data.entities.returntype

import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.util.*

data class TransactionDetail(
    var Transaction_ID: Long,
    var TransactionType_ID: Long,
    var SubCategory_Name: String,
    var Account_Name: String,
    var AccountRecipient_Name: String,
    var Transaction_Amount: Double,
    @TypeConverters(DateTypeConverter::class)
    var Transaction_Date: Date,
    var Person_Name: String,
    var Merchant_Name: String,
    var Transaction_Memo: String,
    var Project_Name: String,
    var Transaction_ReimburseStatus: Int,
    var Period_ID: Long
){
    constructor() : this(
        0,
        0,
        "",
        "",
        "",
        0.0,
        Date(),
        "",
        "",
        "",
        "",
        0,
        0
    )
}