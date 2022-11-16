package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["AccountType_ID"], unique = true)])
data class AccountType (
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(defaultValue = "0")
    var AccountType_ID: Long = 0L,

    @ColumnInfo(defaultValue = "")
    var AccountType_Name: String = "",
    @ColumnInfo(defaultValue = "")
    var AccountType_Memo: String = "",

    @ColumnInfo(defaultValue = "0")
    var AccountType_Parent: Int = 0,

    @ColumnInfo(defaultValue = "0")
    var AccountType_OrderNo: Int = 0
)