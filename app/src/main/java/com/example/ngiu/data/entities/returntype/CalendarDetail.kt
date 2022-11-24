package com.example.ngiu.data.entities.returntype

import java.time.LocalDate

data class CalendarDetail(
    var title: String = "",
    var account_id: Long = 0L,
    var account_out_name: String ="",
    var account_in_name: String ="",
    var account_last_four_number: String="",
    var type: Int = 0,
    //type:1 Credit Card Payment
    //type:2 周期帐
    //type:3 Event
    var category: String ="",
    var amount: Double =0.0,
    var date: LocalDate = LocalDate.now(),
    var mode: Int = 0,
    var memo: String = ""
)
// never used