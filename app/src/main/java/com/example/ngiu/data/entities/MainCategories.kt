package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["ID"],
        childColumns = ["TransTypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["ID"], unique = true)]
)

data class MainCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val ID: Long,
    val TransTypeID: Long,
    @ColumnInfo(name = "Name")
    val Name: String
)