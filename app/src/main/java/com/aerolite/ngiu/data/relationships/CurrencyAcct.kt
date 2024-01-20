package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Currency

data class CurrencyAcct (
    @Embedded val currency: Currency,
    @Relation(
        parentColumn = "Currency_ID",
        entityColumn = "Currency_ID"
    )
    val account: List<Account>
)
