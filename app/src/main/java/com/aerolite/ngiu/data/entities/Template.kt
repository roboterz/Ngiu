package com.aerolite.ngiu.data.entities

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["TransactionType_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Category::class,
        parentColumns = ["Category_ID"],
        childColumns = ["Category_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Account_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["AccountRecipient_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Person::class,
        parentColumns = ["Person_ID"],
        childColumns = ["Person_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Merchant::class,
        parentColumns = ["Merchant_ID"],
        childColumns = ["Merchant_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Project::class,
        parentColumns = ["Project_ID"],
        childColumns = arrayOf("Project_ID"),
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    )
    ],indices = [Index(value = ["Template_ID"], unique = true)])

data class Template(
    @PrimaryKey(autoGenerate = true)
    var Template_ID: Long = 0L,

    // Trans
    @ColumnInfo(defaultValue = "1")
    var TransactionType_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Category_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Account_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var AccountRecipient_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0.00")
    var Transaction_Amount: Double = 0.00,
    @ColumnInfo(defaultValue = "1")
    var Person_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Merchant_ID: Long = 1L,
    @ColumnInfo(defaultValue = "")
    var Transaction_Memo: String = "",
    @ColumnInfo(defaultValue = "1")
    var Project_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0")
    var Transaction_ReimburseStatus: Int = 0
)
