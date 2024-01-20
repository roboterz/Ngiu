package com.aerolite.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["TransactionType_ID"], unique = true)])
data class TransactionType (
    @PrimaryKey(autoGenerate = true)
    var TransactionType_ID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var TransactionType_Name: String = ""
)
