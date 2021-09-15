package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainCategories (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val TypeID: Int,
    val Name: String
)