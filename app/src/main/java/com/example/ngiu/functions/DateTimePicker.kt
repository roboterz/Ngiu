package com.example.ngiu.functions

import android.annotation.SuppressLint
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
import com.google.android.material.dialog.InsetDialogOnTouchListener


class DateTimePicker {

    @SuppressLint("ResourceType")
    fun PickDate(context: Context, dialogOnTouchListener: OnDateSetListener){
        val startYear = Calendar.getInstance().get(Calendar.YEAR)
        val startMonth = Calendar.getInstance().get(Calendar.MONTH)
        val startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        //DatePickerDialog(context,3, DatePickerDialog.OnDateSetListener { _, year, month, day ->
        //}, startYear, startMonth, startDay).show()

        DatePickerDialog(context,3, dialogOnTouchListener, startYear, startMonth, startDay).show()
    }

    fun PickTime(context: Context, dialogOnTouchListener: OnTimeSetListener) {
        val startHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val startMinute = Calendar.getInstance().get(Calendar.MINUTE)

        //TimePickerDialog(context, 3, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
        // }, startHour, startMinute, false).show()

        TimePickerDialog(context, 3, dialogOnTouchListener, startHour, startMinute, false).show()

    }
}