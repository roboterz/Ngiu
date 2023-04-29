package com.example.ngiu.data.entities.returntype

import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.time.format.DateTimeFormatter
import java.util.*

data class PRRecordDetail(
    var Transaction_ID: Long = 0,
    var TransactionType_ID: Long = 0,
    var TransactionType_Name: String = "",
    var Category_ID: Long = 0,
    var Category_Name: String = "",
    var Account_ID: Long = 0,
    var Account_Name: String = "",
    var AccountRecipient_ID: Long = 0,
    var AccountRecipient_Name: String = "",
    var Transaction_Amount: Double = 0.0,
    var Transaction_Amount2: Double = 0.0,
    @TypeConverters(DateTypeConverter::class)
    var Transaction_Date: DateTimeFormatter? = null,
    var Person_Name: String = "",
    var Merchant_Name: String = "",
    var Transaction_Memo: String = "",
    var Project_Name: String = "",
    var Transaction_ReimburseStatus: Int = 0,
    var Period_ID: Long = 0
)
