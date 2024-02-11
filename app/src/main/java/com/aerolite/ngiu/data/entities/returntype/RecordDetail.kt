package com.aerolite.ngiu.data.entities.returntype

import androidx.room.TypeConverters
import com.aerolite.ngiu.data.entities.DateTypeConverter
import java.time.LocalDateTime

data class RecordDetail(
    var Transaction_ID: Long = 0,
    var TransactionType_ID: Long = 0,
    var Category_ID: Long = 0,
    var Category_Name: String = "",
    var Account_ID: Long = 0,
    var Account_Name: String = "",
    var AccountRecipient_ID: Long = 0,
    var AccountRecipient_Name: String = "",
    var Transaction_Amount: Double = 0.0,
    var Transaction_Amount2: Double = 0.0,
    @TypeConverters(DateTypeConverter::class)
    var Transaction_Date: LocalDateTime = LocalDateTime.now(),
    var Transaction_Memo: String = "",

    )