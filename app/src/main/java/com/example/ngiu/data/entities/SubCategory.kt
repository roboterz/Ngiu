package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategory::class,
        parentColumns = ["MainCategory_ID"],
        childColumns = ["MainCategory_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["SubCategory_ID"], unique = true)]
)

data class SubCategory (
    @PrimaryKey(autoGenerate = true)
    var SubCategory_ID: Long = 0,
    @ColumnInfo(defaultValue = "1")
    var MainCategory_ID: Long = 1,
    @ColumnInfo(defaultValue = "")
    var SubCategory_Name: String = "",
    @ColumnInfo(defaultValue = "false")
    var SubCategory_Common: Boolean = false
)