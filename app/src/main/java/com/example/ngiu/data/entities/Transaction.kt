package com.example.ngiu.data.entities

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["trans_type_id"],
        childColumns = ["TypeID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = MainCategories::class,
        parentColumns = ["main_cat_id"],
        childColumns = ["CategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["acct_id"],
        childColumns = ["PayerID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["acct_id"],
        childColumns = ["RecipientID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Individual::class,
        parentColumns = ["indiv_id"],
        childColumns = ["IndividualID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Merchant::class,
        parentColumns = ["merchant_id"],
        childColumns = ["MerchantID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Project::class,
        parentColumns = ["project_id"],
        childColumns = arrayOf("ProjectID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Period::class,
        parentColumns = ["period_id"],
        childColumns = ["PeriodID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )
    ], indices = [Index(value = ["trans_id"], unique = true)]
)

data class Transaction (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trans_id")
    val id: Int,
    val TypeID: Int,
    val CategoryID: Int,
    val PayerID: Int,
    val RecipientID: Int,
    val Amount: Double,
    @TypeConverters(DateTypeConverter::class)
    val Date: Date,
    val IndividualID: Int,
    val MerchantID: Int,
    val Memo: Char,
    val ProjectID: Int,
    val ReimburseStatus: Boolean,
    val PeriodID: Int
)
