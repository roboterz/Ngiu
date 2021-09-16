package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "acctId")
    val id: Int,
    @ColumnInfo(name = "acctType")
    val TypeID: Int,
    @ColumnInfo(name = "acctName")
    val Name: String,
    val BaseCurrency: Char
)

