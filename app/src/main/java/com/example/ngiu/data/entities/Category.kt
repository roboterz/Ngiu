package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionType::class,
        parentColumns = ["TransactionType_ID"],
        childColumns = ["TransactionType_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Icon::class,
        parentColumns = ["Icon_ID"],
        childColumns = ["Icon_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )], indices = [Index(value = ["Category_ID"], unique = true)]
)

data class Category(
    @PrimaryKey(autoGenerate = true)
    var Category_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var TransactionType_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var Category_ParentID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var Category_Name: String = "",
    @ColumnInfo(defaultValue = "false")
    var Category_Common: Boolean = false,

    @ColumnInfo(defaultValue = "0")
    var Category_OrderNo: Int = 0,
    @ColumnInfo(defaultValue = "false")
    var Category_IsDefault: Boolean = false,
    @ColumnInfo(defaultValue = "1")
    var Icon_ID: Long = 1L,

    @ColumnInfo(defaultValue = "false")
    var Category_IsDelete: Boolean = false,
    @ColumnInfo(defaultValue = "false")
    var Category_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Category_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Category_UploadTime: LocalDateTime = LocalDateTime.now()

)
