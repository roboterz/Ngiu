package com.example.ngiu.data.entities.returntype

import java.time.LocalDate

data class CalendarDetail(
    var name: String = "",
    var account_out_name: String ="",
    var account_in_name: String ="",
    var account_last_four_number: String="",
    var type: Int = 0,
    //type:1 Credit Card Payment
    //type:2
    //type:3
    var category: String ="",
    var amount: Double =0.0,
    var date: LocalDate = LocalDate.now()
)
// never used