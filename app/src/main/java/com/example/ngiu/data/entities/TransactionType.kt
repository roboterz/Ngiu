package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["trans_type_id"], unique = true)])
data class TransactionType (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trans_type_id")
    val id: Int,
    @ColumnInfo(name = "trans_type_name")
    val Name: String
)
