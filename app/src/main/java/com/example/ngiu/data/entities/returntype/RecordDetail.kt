package com.example.ngiu.data.entities.returntype

import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.util.*

data class RecordDetail(
    val Transaction_ID: Long = 0,
    val TransactionType_ID: Long = 0,
    val TransactionType_Name: String = "",
    val SubCategory_ID: Long = 0,
    val SubCategory_Name: String = "",
    val Account_ID: Long = 0,
    val Account_Name: String = "",
    val AccountRecipient_ID: Long = 0,
    val AccountRecipient_Name: String = "",
    val Transaction_Amount: Double = 0.0,
    @TypeConverters(DateTypeConverter::class)
    val Transaction_Date: Date,
    val Person_Name: String = "",
    val Merchant_Name: String = "",
    val Transaction_Memo: String = "",
    val Project_Name: String = "",
    val Transaction_ReimburseStatus: Int = 0,
    val Period_ID: Long = 0
)
