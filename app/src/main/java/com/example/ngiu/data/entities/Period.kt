package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Period (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "periodId")
    val id: Int,
    @ColumnInfo(name = "periodType")
    val TypeID: Int,
    val TransactionID: Int,
    val RepeatInterval: Int,
    val Status: Boolean,
    val EndDate: Date,
    val Name: String
)