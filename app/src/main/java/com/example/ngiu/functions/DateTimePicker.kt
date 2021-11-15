package com.example.ngiu.functions

import android.annotation.SuppressLint

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import java.util.*

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context


class DateTimePicker(
    private val startYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    private val startMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    private val startDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    private val startHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    private val startMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
) {

    @SuppressLint("ResourceType")
    fun pickDate(context: Context, dialogOnTouchListener: OnDateSetListener){

        //DatePickerDialog(context,3, DatePickerDialog.OnDateSetListener { _, year, month, day ->
        //}, startYear, startMonth, startDay).show()

        DatePickerDialog(context,3, dialogOnTouchListener, startYear, startMonth, startDay).show()
    }

    fun pickTime(context: Context?, dialogOnTouchListener: OnTimeSetListener) {

        //TimePickerDialog(context, 3, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        // }, startHour, startMinute, false).show()

        TimePickerDialog(context, 3, dialogOnTouchListener, startHour, startMinute, false).show()

    }
}