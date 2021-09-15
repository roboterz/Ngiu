package com.example.ngiu.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val MainCategoryID: Int,
    val Name: String
)