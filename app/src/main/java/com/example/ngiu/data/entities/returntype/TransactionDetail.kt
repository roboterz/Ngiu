package com.example.ngiu.data.entities.returntype

import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

data class TransactionDetail(
    var Transaction_ID: Long = 0L,
    var TransactionType_ID: Long = 0L,
    var Category_ID: Long = 0L,
    var Category_Name: String = "",
    var Account_ID: Long = 0L,
    var Account_Name: String = "",
    var AccountRecipient_ID: Long = 0L,
    var AccountRecipient_Name: String = "",
    var Transaction_Amount: Double = 0.00,
    var Transaction_Amount2: Double = 0.00,
    @TypeConverters(DateTypeConverter::class)
    var Transaction_Date: LocalDateTime = LocalDateTime.now(),
    var Person_Name: String = "",
    var Merchant_Name: String = "",
    var Transaction_Memo: String = "",
    var Project_Name: String = "",
    var Transaction_ReimburseStatus: Int = 0,
    var Period_ID: Long = 0L
)