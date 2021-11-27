package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["AccountType_ID"], unique = true)])
data class AccountType (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val AccountType_ID: Long = 0,
    //@ColumnInfo(name = "Acct_Type_Name")
    @ColumnInfo(defaultValue = "")
    val AccountType_Name: String = "",
    @ColumnInfo(defaultValue = "")
    val AccountType_Memo: String = ""
)