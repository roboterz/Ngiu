package com.example.ngiu.data.entities

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["ID"],
        childColumns = ["TransTypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Project::class,
        parentColumns = ["ID"],
        childColumns = ["ProjectID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["ID"],
        childColumns = ["MerchantID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Person::class,
        parentColumns = ["ID"],
        childColumns = ["PersonID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SubCategories::class,
        parentColumns = ["ID"],
        childColumns = ["SubCategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["ID"], unique = true)],
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long = 0,
    val RepeatInterval: Int = 0,
    val EndStatus: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    val StarDate: Date,
    @TypeConverters(DateTypeConverter::class)
    val EndDate: Date,
    val TransTypeID: Long = 0,
    val SubCategoryID: Long = 0,
    val PayerID: Long = 0,
    val RecipientID: Long = 0,
    val Amount: Double=0.0,
    val PersonID: Long = 0,
    val MerchantID: Long = 0,
    val ProjectID: Long = 0,
    val ReimburseStatus: Int = 0,
    val Memo: String = ""







)