package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = ["acct_type_id"],
        childColumns = ["TypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Currency::class,
        parentColumns = ["currency_id"],
        childColumns = ["BaseCurrency"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )], indices = [Index(value = ["acct_id"], unique = true)]
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




