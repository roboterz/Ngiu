package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ID"], unique = true)])
data class Currency(
    @PrimaryKey()
    //@ColumnInfo(name = "ID")
    val ID: String="",
    //@ColumnInfo(name = "Name")
    val Name: String="",
    val ExchangeRate: Double=0.00
)