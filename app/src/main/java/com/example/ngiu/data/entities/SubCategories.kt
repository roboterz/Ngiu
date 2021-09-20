package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategories::class,
        parentColumns = ["main_cat_id"],
        childColumns = ["MainCategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["sub_cat_id"], unique = true)]
)

data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sub_cat_id")
    val id: Int,
    val MainCategoryID: Int,
    @ColumnInfo(name = "sub_cat_name")
    val Name: String
)