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
    val ID: Long = 0,
    val AcctTypeID: Long = 0,
    //@ColumnInfo(name = "Name")
    val Name: String = "",
    // balance | current arrears | value
    val Balance: Double = 0.0,
    val CountInNetAssets: Boolean = true,
    val Memo: String = "",
    val BaseCurrency: String = "",

    //credit card part -------

    //card number | user ID
    val CardNumber: String = "",
    val StatementDay: Int = 1,
    val CreditLimit: Double = 0.0,
    val FixedPaymentDay: Boolean = true,
    val PaymentDay: Int = 20,
    val Interval: Int = 20,
    val Reminder: Int = 0,
    val StatisticalMode: Int = 0

)