package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["TransactionType_ID"],
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
        entity = Project::class,
        parentColumns = ["Project_ID"],
        childColumns = ["Project_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["Merchant_ID"],
        childColumns = ["Merchant_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Person::class,
        parentColumns = ["Person_ID"],
        childColumns = ["Person_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Category::class,
        parentColumns = ["Category_ID"],
        childColumns = ["Category_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["Period_ID"], unique = true)],
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Period_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var Period_RepeatInterval: Int = 0,
    @ColumnInfo(defaultValue = "0")
    var Period_EndStatus: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    var Period_StarDate: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Period_EndDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(defaultValue = "1")
    var TransactionType_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Category_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Account_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var AccountRecipient_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0.00")
    var Period_Amount: Double = 0.00,
    @ColumnInfo(defaultValue = "1")
    var Person_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Merchant_ID: Long = 1L,
    @ColumnInfo(defaultValue = "1")
    var Project_ID: Long = 1L,
    @ColumnInfo(defaultValue = "0")
    var Period_ReimburseStatus: Int = 0,
    @ColumnInfo(defaultValue = "")
    var Period_Memo: String = "",

    @TypeConverters(DateTypeConverter::class)
    var Period_LastActionTime: LocalDateTime = LocalDateTime.now(),

    var Period_OrderNo: Int = 0,

    var Period_IsDelete: Boolean = false,

    var Period_UploadStatus: Boolean = false,

    @TypeConverters(DateTypeConverter::class)
    var Period_CreateTime: LocalDateTime = LocalDateTime.now(),

    @TypeConverters(DateTypeConverter::class)
    var Period_UploadTime: LocalDateTime = LocalDateTime.now()
)