package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountType (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val Name: String
)