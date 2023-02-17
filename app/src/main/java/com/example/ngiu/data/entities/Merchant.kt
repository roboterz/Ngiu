package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Merchant_ID"], unique = true)])
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Merchant_ID: Long,
    //@ColumnInfo(name = "Name")
    var Merchant_Name: String
)