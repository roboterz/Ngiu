package com.example.ngiu.data.entities.returntype

import android.graphics.Bitmap
import androidx.room.ColumnInfo

data class AccountIcon(
    var Account_ID: Long = 0L,
    var Account_Name: String = "",
    var Icon_ID: Long = 0L,
    var Icon_Name: String = "",
    var Icon_Path: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var Icon_Image: Bitmap? = null
)
