package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountType (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "acct_type_id")
    val id: Int,
    @ColumnInfo(name = "acct_type_name")
    val Name: String
)