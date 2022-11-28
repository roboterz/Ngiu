package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(indices = [Index(value = ["Merchant_ID"], unique = true)])
data class Merchant (
    @PrimaryKey(autoGenerate = true)
    var Merchant_ID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var Merchant_Name: String = "",
    @ColumnInfo(defaultValue = "0")
    var Merchant_OrderNo: Int = 0,
    @ColumnInfo(defaultValue = "false")
    var Merchant_Star: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Merchant_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Merchant_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Merchant_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Merchant_UploadTime: LocalDateTime = LocalDateTime.now()
)