package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionType (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trans_type_id")
    val id: Int,
    @ColumnInfo(name = "trans_type_name")
    val Name: String
)