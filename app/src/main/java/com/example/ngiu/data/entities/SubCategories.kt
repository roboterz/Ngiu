package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sub_cat_id")
    val id: Int,
    val MainCategoryID: Int,
    val Name: String
)