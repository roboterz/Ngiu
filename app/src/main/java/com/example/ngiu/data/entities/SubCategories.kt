package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = MainCategories::class,
        parentColumns = ["ID"],
        childColumns = ["MainCategoryID"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["ID"], unique = true)]
)

data class SubCategories (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long=0,
    val MainCategoryID: Long=0,
    //@ColumnInfo(name = "Name")
    val Name: String=""
)