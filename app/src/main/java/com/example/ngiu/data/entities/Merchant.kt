package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "merchant_id")
    val id: Int,
    @ColumnInfo(name = "merchant_name")
    val Name: String
)