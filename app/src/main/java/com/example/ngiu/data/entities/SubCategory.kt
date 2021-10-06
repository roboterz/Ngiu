package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategory::class,
        parentColumns = ["MainCategory_ID"],
        childColumns = ["MainCategory_ID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["SubCategory_ID"], unique = true)]
)

data class SubCategory (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val SubCategory_ID: Long = 0,
    val MainCategory_ID: Long = 0,
    //@ColumnInfo(name = "Name")
    val SubCategory_Name: String = ""
)