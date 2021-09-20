package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["currency_id"], unique = true)])
data class Currency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "currency_id")
    val id: Int,
    @ColumnInfo(name = "currency_name")
    val Name: String,
    val ExchangeRate: Double
)