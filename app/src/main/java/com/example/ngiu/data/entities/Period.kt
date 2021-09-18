package com.example.ngiu.data.entities

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = arrayOf("acct_type_id"),
        childColumns = arrayOf("TypeID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = TransactionType::class,
        parentColumns = arrayOf("trans_type_id"),
        childColumns = arrayOf("TransactionID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE,
    )]
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "period_id")
    val id: Int,
    val TypeID: Int,
    val TransactionID: Int,
    val RepeatInterval: Int,
    val Status: Boolean,
    @TypeConverters(DateTypeConverter::class)
    val EndDate: Date,
    @ColumnInfo(name = "period_name")
    val Name: String
)