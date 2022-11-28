package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = ["Category_ID"],
    childColumns = ["Category_ID"],
    onDelete = ForeignKey.RESTRICT,
    onUpdate = ForeignKey.CASCADE
)], indices = [Index(value = ["Budget_ID"], unique = true)])

data class Budget(
    @PrimaryKey(autoGenerate = true)
    var Budget_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var Budget_Type: Int = 0,
    @ColumnInfo(defaultValue = "0")
    var Category_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0.00")
    var Budget_Amount: Double = 0.00,

    @ColumnInfo(defaultValue = "false")
    var Budget_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Budget_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Budget_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Budget_UploadTime: LocalDateTime = LocalDateTime.now()
)
