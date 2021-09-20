package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["merchant_id"], unique = true)])
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "merchant_id")
    val id: Int,
    @ColumnInfo(name = "merchant_name")
    val Name: String
)