package com.example.ngiu.data.entities

import androidx.room.TypeConverter
import java.util.*

/*
https://developer.android.com/training/data-storage/room/referencing-data#kotlin
Room doesn't know how to persist date objects.
*/

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}