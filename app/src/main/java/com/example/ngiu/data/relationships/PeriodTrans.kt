package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Transaction

data class PeriodTrans (
    @Embedded val period: Period,
    @Relation(
        parentColumn = "period_id",
        entityColumn = "PeriodID"
    )
    val transaction: List<Transaction>
)

data class AcctPeriod (
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "PayerID"
    )
    val period: List<Period>
)

data class AcctRecipient (
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "RecipientID"
    )
    val period: List<Period>
)