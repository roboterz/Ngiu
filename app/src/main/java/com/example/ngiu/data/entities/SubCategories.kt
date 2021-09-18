package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategories::class,
        parentColumns = arrayOf("main_cat_id"),
        childColumns = arrayOf("MainCategoryID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sub_cat_id")
    val id: Int,
    val MainCategoryID: Int,
    @ColumnInfo(name = "sub_cat_name")
    val Name: String
)