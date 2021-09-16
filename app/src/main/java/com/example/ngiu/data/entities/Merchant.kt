package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "merchantId")
    val id: Int,
    @ColumnInfo(name = "merchantName")
    val Name: String
)