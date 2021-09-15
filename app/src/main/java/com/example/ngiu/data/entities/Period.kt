package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Period (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val TypeID: Int,
    val TransactionID: Int,
    val RepeatInterval: Int,
    val Status: Boolean,
    val EndDate: Date,
    val Name: String
)