package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(indices = [Index(value = ["Person_ID"], unique = true)])
data class Person (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Person_ID: Long = 0L,
    //@ColumnInfo(name = "name")
    var Person_Name: String = "",

    var Person_OrderNo: Int = 0,

    var Person_UploadStatus: Boolean = false,

    var Person_IsDefault: Boolean = false,

    var Person_IsDelete: Boolean = false,

    @TypeConverters(DateTypeConverter::class)
    var Person_CreateTime: LocalDateTime = LocalDateTime.now(),

    @TypeConverters(DateTypeConverter::class)
    var Person_UploadTime: LocalDateTime = LocalDateTime.now()
)