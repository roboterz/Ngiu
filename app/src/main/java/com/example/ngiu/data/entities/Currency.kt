package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Currency_ID"], unique = true)])
data class Currency(
    @PrimaryKey()
    //@ColumnInfo(name = "ID")
    var Currency_ID: String,
    //@ColumnInfo(name = "Name")
    var Currency_Name: String,
    var Currency_ExchangeRate: Double
)