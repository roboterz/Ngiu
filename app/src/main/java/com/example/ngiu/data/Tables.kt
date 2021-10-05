package com.example.ngiu.data

import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.util.*

data class Tables(
    val ID: Long = 0,
    val TransType: String= "",
    val SubCategory: String = "",
    val Payer: String = "",
    val Recipient: String = "",
    val Amount: Double = 0.0,
    @TypeConverters(DateTypeConverter::class)
    val Date: Date,
    val Person: String = "",
    val Merchant: String = "",
    val Memo: String = "",
    val Project: String = "",
    val ReimburseStatus: Int = 0,
    val PeriodID: Long = 0
)