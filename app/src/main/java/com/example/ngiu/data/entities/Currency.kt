package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Currency_ID"], unique = true)])
data class Currency(
    @PrimaryKey()
    //@ColumnInfo(name = "ID")
    val Currency_ID: String,
    //@ColumnInfo(name = "Name")
    val Currency_Name: String,
    val Currency_ExchangeRate: Double
)