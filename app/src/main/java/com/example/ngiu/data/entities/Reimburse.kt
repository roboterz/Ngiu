package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reimburse (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val TransactionID: Int,
    val Status: Boolean
)