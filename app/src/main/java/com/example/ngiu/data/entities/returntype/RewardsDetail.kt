package com.example.ngiu.data.entities.returntype

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ngiu.data.entities.DateTypeConverter
import java.time.LocalDateTime

data class RewardsDetail(
    var Reward_ID: Long = 0L,
    var Account_ID: Long = 0L,
    var Category_ID: Long = 0L,
    var Category_Name: String = "",
    var Merchant_ID: Long = 0L,
    var Merchant_Name: String = "",
    var Reward_Mode: Int = 0,
    var Reward_Percentage: Double = 1.0,
    @TypeConverters(DateTypeConverter::class)
    var Reward_StartDate: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Reward_EndDate: LocalDateTime = LocalDateTime.now(),

    var Icon_ID: Long = 1L,
    var Icon_Path: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var Icon_Image: Bitmap? = null

)
