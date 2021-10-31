package com.example.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = ["AccountType_ID"],
        childColumns = ["AccountType_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Currency::class,
        parentColumns = ["Currency_ID"],
        childColumns = ["Currency_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )], indices = [Index(value = ["Account_ID"], unique = true)]
)

data class Account(
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val Account_ID: Long = 0,
    val AccountType_ID: Long = 0,
    //@ColumnInfo(name = "Name")
    val Account_Name: String = "",
    // balance | current arrears | value
    val Account_Balance: Double = 0.0,
    val Account_CountInNetAssets: Boolean = true,
    val Account_Memo: String = "",
    val Currency_ID: String = "",

    //credit card part -------

    //card number | user ID
    val Account_CardNumber: String = "",
    val Account_StatementDay: Int = 1,
    val Account_CreditLimit: Double = 0.0,
    val Account_FixedPaymentDay: Boolean = true,
    val Account_PaymentDay: Int = 25,
    val Account_Interval: Int = 25,
    val Account_Reminder: Int = 0,
    val Account_StatisticalMode: Int = 0




)