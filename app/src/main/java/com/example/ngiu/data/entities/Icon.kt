package com.example.ngiu.data.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value = ["Icon_ID"], unique = true)])
data class Icon(
    @PrimaryKey(autoGenerate = true)
    var Icon_ID: Long = 0L,
    var Icon_Path: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var Icon_Image: Bitmap? = null
)
