package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MainCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "main_cat_id")
    val id: Int,
    @ColumnInfo(name = "main_cat_type")
    val TypeID: Int,
    @ColumnInfo(name = "main_cat_name")
    val Name: String
)