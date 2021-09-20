package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["trans_type_id"],
        childColumns = ["TypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["main_cat_id"], unique = true)]
)

data class MainCategories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "main_cat_id")
    val id: Int,
    val TypeID: Int,
    @ColumnInfo(name = "main_cat_name")
    val Name: String
)