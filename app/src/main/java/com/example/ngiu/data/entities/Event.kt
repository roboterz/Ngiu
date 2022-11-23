package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime


@Entity(indices = [Index(value = ["Event_ID"], unique = true)])
data class Event(
    @PrimaryKey(autoGenerate = true)
    var Event_ID: Long = 0,
    var Event_Memo: String = "",

    //Mode: 0-> all day;
    var Event_Mode: Int = 0,
    @TypeConverters(DateTypeConverter::class)
    var Event_Date: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(defaultValue = "false")
    var Event_Complete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Event_UploadStatus: Boolean = false,
    @ColumnInfo(defaultValue = "false")
    var Event_IsDelete: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Event_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Event_UploadTime: LocalDateTime = LocalDateTime.now()
)
