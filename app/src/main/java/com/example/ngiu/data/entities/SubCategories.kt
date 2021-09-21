package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategories::class,
        parentColumns = ["ID"],
        childColumns = ["MainCategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["ID"], unique = true)]
)

data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long,
    val MainCategoryID: Long,
    //@ColumnInfo(name = "Name")
    val Name: String
)