package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class record(
    val id: Long,
    val name: String,
    val type: String,
    val category: String,
    val payer: String,
    val recipient: String,
    val amount: Double,
    val date: Date,
    val person: String,
    val merchant: String,
    val memo: String,
    val project: String

    // join all tables and load the
)