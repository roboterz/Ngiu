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
    val ID: Long,
    val RepeatInterval: Int,
    val Status: Boolean,
    @TypeConverters(DateTypeConverter::class)
    val StarDate: Date,
    @TypeConverters(DateTypeConverter::class)
    val EndDate: Date,
    val TransTypeID: Long,
    val SubCategoryID: Long,
    val PayerID: Long,
    val RecipientID: Long,
    val Amount: Double,
    val PersonID: Long,
    val MerchantID: Long,
    val ProjectID: Long,
    val ReimburseStatus: Int,
    val Memo: String







)