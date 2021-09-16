package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transId")
    val id: Int,
    @ColumnInfo(name = "trans_type")
    val TypeID: Int,
    val CategoryID: Int,
    val PayerID: Int,
    val RecipientID: Int,
    val Amount: Double,
    val Date: Date,
    val IndividualID: Int,
    val MerchantID: Int,
    val Memo: Char,
    val ProjectID: Int,
    val ReimburseID: Int,
    val PeriodID: Int
)