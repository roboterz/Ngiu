package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ID"], unique = true)])
data class AccountType (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long = 0,
    //@ColumnInfo(name = "Acct_Type_Name")
    val Name: String = "",
    var Memo: String = ""
)