package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(indices = [Index(value = ["Currency_ID"], unique = true)])
data class Currency(
    @PrimaryKey()
    var Currency_ID: String = "",
    @ColumnInfo(defaultValue = "")
    var Currency_Name: String = "",
    @ColumnInfo(defaultValue = "")
    var Currency_ShortName: String = "",
    @ColumnInfo(defaultValue = "1.0")
    var Currency_ExchangeRate: Double = 1.0,
    @ColumnInfo(defaultValue = "0")
    var Currency_OrderNo: Int = 0,

    @ColumnInfo(defaultValue = "false")
    var Currency_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Currency_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Currency_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Currency_UploadTime: LocalDateTime = LocalDateTime.now()
)