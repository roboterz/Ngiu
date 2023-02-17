package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["Period_TransactionType_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Period_Account_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Period_AccountRecipient_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Project::class,
        parentColumns = ["Project_ID"],
        childColumns = ["Period_Project_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["Merchant_ID"],
        childColumns = ["Period_Merchant_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Person::class,
        parentColumns = ["Person_ID"],
        childColumns = ["Period_Person_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SubCategory::class,
        parentColumns = ["SubCategory_ID"],
        childColumns = ["Period_SubCategory_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["Period_ID"], unique = true)],
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Period_ID: Long = 0,
    @ColumnInfo(defaultValue = "0")
    var Period_RepeatInterval: Int = 0,
    @ColumnInfo(defaultValue = "0")
    var Period_EndStatus: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    var Period_StarDate: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Period_EndDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(defaultValue = "1")
    var Period_TransactionType_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Period_SubCategory_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Period_Account_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Period_AccountRecipient_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0.00")
    var Period_Amount: Double = 0.00,
    @ColumnInfo(defaultValue = "1")
    var Period_Person_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Period_Merchant_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Period_Project_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0")
    var Period_ReimburseStatus: Int = 0,
    @ColumnInfo(defaultValue = "")
    var Period_Memo: String = ""







)