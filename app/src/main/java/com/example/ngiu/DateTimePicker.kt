package com.example.ngiu

import android.app.Activity
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import java.util.*
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.widget.Toast


class DateTimePicker {
    class reTime{
        var Hour: Int = 0
        var Minute: Int = 0
    }

    fun PickDate(context: Context){
        val startYear = Calendar.getInstance().get(Calendar.YEAR)
        val startMonth = Calendar.getInstance().get(Calendar.MONTH)
        val startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context,3, DatePickerDialog.OnDateSetListener { _, year, month, day ->

        }, startYear, startMonth, startDay).show()
    }

    fun PickTime(context: Context) : reTime {
        val startHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val startMinute = Calendar.getInstance().get(Calendar.MINUTE)
        val rt: reTime = reTime()

        TimePickerDialog(context, 3,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            //val pickedDateTime = Calendar.getInstance()
            //pickedDateTime.set(hour, minute)
            //doSomethingWith(pickedDateTime)
            //Toast.makeText(context ,hour.toString() + ":" + minute.toString(), Toast.LENGTH_SHORT).show()
            rt.Hour = hour
            rt.Minute = minute

        }, startHour, startMinute, false).show()

        return rt
    }
}