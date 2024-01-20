package com.aerolite.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime


@Entity(indices = [Index(value = ["Event_ID"], unique = true)])
data class Event(
    @PrimaryKey(autoGenerate = true)
    var Event_ID: Long = 0,
    @ColumnInfo(defaultValue = "")
    var Event_Memo: String = "",

    //Mode: 0-> all day;
    @ColumnInfo(defaultValue = "0")
    var Event_Mode: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    var Event_Date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(defaultValue = "false")
    var Event_Complete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Event_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Event_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Event_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Event_UploadTime: LocalDateTime = LocalDateTime.now()
)
