package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = arrayOf("trans_type_id"),
        childColumns = arrayOf("TypeID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class MainCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "main_cat_id")
    val id: Int,
    val TypeID: Int,
    @ColumnInfo(name = "main_cat_name")
    val Name: String
)