package com.example.ngiu.data.entities

import android.graphics.Bitmap
import androidx.room.*
import java.time.LocalDateTime


@Entity(indices = [Index(value = ["Icon_ID"], unique = true)])
data class Icon(
    @PrimaryKey(autoGenerate = true)
    var Icon_ID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var Icon_Name: String = "",
    @ColumnInfo(defaultValue = "0")
    var Icon_Type: Int = 0,
    @ColumnInfo(defaultValue = "")
    var Icon_Path: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var Icon_Image: Bitmap? = null,


    @ColumnInfo(defaultValue = "false")
    var Icon_IsDelete: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var Icon_UploadStatus: Boolean = false,
    @TypeConverters(DateTypeConverter::class)
    var Icon_CreateTime: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Icon_UploadTime: LocalDateTime = LocalDateTime.now()
)
