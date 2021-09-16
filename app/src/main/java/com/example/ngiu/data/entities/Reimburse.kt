package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reimburse (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reimburseId")
    val id: Int,
    val TransactionID: Int,
    val Status: Boolean
)