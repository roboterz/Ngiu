package com.aerolite.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(indices = [Index(value = ["Project_ID"], unique = true)])
data class Project (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Project_ID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var Project_Name: String = "",
    @ColumnInfo(defaultValue = "0")
    var Project_OrderNo: Int = 0,
    @ColumnInfo(defaultValue = "false")
    var Project_IsDefault: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Project_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Project_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Project_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Project_UploadTime: LocalDateTime = LocalDateTime.now()
)