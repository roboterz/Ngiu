package com.example.ngiu.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["Account_ID"],
        childColumns = ["Account_ID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Category::class,
        parentColumns = ["Category_ID"],
        childColumns = ["Category_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = Merchant::class,
        parentColumns = ["Merchant_ID"],
        childColumns = ["Merchant_ID"],
        onDelete = ForeignKey.SET_DEFAULT,
        onUpdate = ForeignKey.CASCADE,
    )], indices = [Index(value = ["Reward_ID"], unique = true)])
data class Reward(
    @PrimaryKey(autoGenerate = true)
    var Reward_ID: Long = 0L,
    var Account_ID: Long = 0L,
    var Category_ID: Long = 0L,
    var Merchant_ID: Long = 0L,
    var Reward_Percentage: Double = 1.0,
    @TypeConverters(DateTypeConverter::class)
    var Reward_StartDate: LocalDateTime = LocalDateTime.now(),
    @TypeConverters(DateTypeConverter::class)
    var Reward_EndDate: LocalDateTime = LocalDateTime.now()
)
