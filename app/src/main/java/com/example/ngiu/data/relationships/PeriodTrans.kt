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

data class TransPeriod (
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "trans_id",
        entityColumn = "TransactionID"
    )
    val period: List<Period>
)