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
    ),ForeignKey(
        entity = SubCategories::class,
        parentColumns = ["ID"],
        childColumns = ["SubCategoryID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["ID"],
        childColumns = ["PayerID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Account::class,
        parentColumns = ["ID"],
        childColumns = ["RecipientID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Person::class,
        parentColumns = ["ID"],
        childColumns = ["PersonID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Merchant::class,
        parentColumns = ["ID"],
        childColumns = ["MerchantID"],
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Project::class,
        parentColumns = ["ID"],
        childColumns = arrayOf("ProjectID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )
    ], indices = [Index(value = ["ID"], unique = true)]
)

data class Trans(
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val ID: Long = 0,
    val TransTypeID: Long = 0,
    val SubCategoryID: Long = 0,
    val PayerID: Long = 0,
    val RecipientID: Long = 0,
    val Amount: Double = 0.0,
    @TypeConverters(DateTypeConverter::class)
    val Date: Date,
    val PersonID: Long = 0,
    val MerchantID: Long = 0,
    val Memo: String = "",
    val ProjectID: Long = 0,
    val ReimburseStatus: Int = 0,
    val PeriodID: Long = 0
)/*{
    constructor( ): this(0,0,0,0,0,0.0,null,0,0,"",0,false,0)
}*/
