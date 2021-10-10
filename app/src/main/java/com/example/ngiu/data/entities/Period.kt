package com.example.ngiu.data.entities

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["Period_TransactionType_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Period_Account_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Period_AccountRecipient_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Project::class,
        parentColumns = ["Project_ID"],
        childColumns = ["Period_Project_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["Merchant_ID"],
        childColumns = ["Period_Merchant_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Person::class,
        parentColumns = ["Person_ID"],
        childColumns = ["Period_Person_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SubCategory::class,
        parentColumns = ["SubCategory_ID"],
        childColumns = ["Period_SubCategory_ID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["Period_ID"], unique = true)],
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val Period_ID: Long = 0,
    val Period_RepeatInterval: Int = 0,
    val Period_EndStatus: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    val Period_StarDate: Date,
    @TypeConverters(DateTypeConverter::class)
    val Period_EndDate: Date,
    val Period_TransactionType_ID: Long = 0,
    val Period_SubCategory_ID: Long = 0,
    val Period_Account_ID: Long = 0,
    val Period_AccountRecipient_ID: Long = 0,
    val Period_Amount: Double=0.0,
    val Period_Person_ID: Long = 0,
    val Period_Merchant_ID: Long = 0,
    val Period_Project_ID: Long = 0,
    val Period_ReimburseStatus: Int = 0,
    val Period_Memo: String = ""







)