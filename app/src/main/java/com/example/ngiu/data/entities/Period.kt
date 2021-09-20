package com.example.ngiu.data.entities

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = AccountType::class,
        parentColumns = ["acct_type_id"],
        childColumns = ["TypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Project::class,
        parentColumns = ["project_id"],
        childColumns = ["ProjectID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["merchant_id"],
        childColumns = ["MerchantID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Individual::class,
        parentColumns = ["indiv_id"],
        childColumns = ["IndividualID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = SubCategories::class,
        parentColumns = ["sub_cat_id"],
        childColumns = ["CategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["period_id"], unique = true)],
)
data class Period (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "period_id")
    val id: Int,
    val RepeatInterval: Int,
    val Status: Boolean,
    @TypeConverters(DateTypeConverter::class)
    val StarDate: Date,
    @TypeConverters(DateTypeConverter::class)
    val EndDate: Date,
    val TypeID: Int,
    val CategoryID: Int,
    val PayerID: Int,
    val RecipientID: Int,
    val Amount: Double,
    val IndividualID: Int,
    val MerchantID: Int,
    val ProjectID: Int,
    val ReimburseStatus: Boolean,
    val Memo: String







)