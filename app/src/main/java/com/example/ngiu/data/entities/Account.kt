package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = ["ID"],
        childColumns = ["AcctTypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Currency::class,
        parentColumns = ["ID"],
        childColumns = ["BaseCurrency"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )], indices = [Index(value = ["ID"], unique = true)]
)

data class Account (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long,
    val AcctTypeID: Long,
    //@ColumnInfo(name = "Name")
    val Name: String,
    val BaseCurrency: String
)




