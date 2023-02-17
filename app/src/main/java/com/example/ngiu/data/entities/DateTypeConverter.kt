package com.example.ngiu.data.entities

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class DateTypeConverter {
    /*
    @TypeConverter
    fun fromTimestamp(value: Long): Calendar? {
        val cal = Calendar.getInstance()
        cal.timeInMillis = value
        return cal
    }

    @TypeConverter
    fun dateToTimestamp(date: Calendar?): Long? {
        if (date != null) {
            return date.time.time
        }else{
            return 0
        }
    }

     */




    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
         return LocalDateTime.parse(value, formatter)

    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }


}