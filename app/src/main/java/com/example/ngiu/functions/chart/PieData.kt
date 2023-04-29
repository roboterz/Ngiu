package com.example.ngiu.functions.chart

data class PieData(
    val type:String,
    val value:Float,
    val color:String,
    val on_select_color:String = "#00000000",
    var isSelected:Boolean = false
    )