package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Merchant_ID"], unique = true)])
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val Merchant_ID: Long = 0,
    //@ColumnInfo(name = "Name")
    val Merchant_Name: String = ""
)