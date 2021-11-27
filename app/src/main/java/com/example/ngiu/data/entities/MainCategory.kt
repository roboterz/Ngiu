package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["TransactionType_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["MainCategory_ID"], unique = true)]
)

data class MainCategory (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val MainCategory_ID: Long = 0,
    @ColumnInfo(defaultValue = "1")
    var TransactionType_ID: Long = 1,
    @ColumnInfo(defaultValue = "")
    var MainCategory_Name: String = ""
)