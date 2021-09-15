package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
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