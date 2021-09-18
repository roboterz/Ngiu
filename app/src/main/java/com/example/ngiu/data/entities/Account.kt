package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = arrayOf("acct_type_id"),
        childColumns = arrayOf("TypeID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Currency::class,
        parentColumns = arrayOf("currency_id"),
        childColumns = arrayOf("BaseCurrency"),
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )]
)

data class Account (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "acct_id")
    val id: Int,
    val TypeID: Int,
    @ColumnInfo(name = "acct_name")
    val Name: String,
    val BaseCurrency: Char
)

