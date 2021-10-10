package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Currency

data class CurrencyAcct (
    @Embedded val currency: Currency,
    @Relation(
        parentColumn = "Currency_ID",
        entityColumn = "Currency_ID"
    )
    val account: List<Account>
)
