package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Trans

data class PeriodTrans (
    @Embedded val period: Period,
    @Relation(
        parentColumn = "period_id",
        entityColumn = "PeriodID"
    )
    val trans: List<Trans>
)

data class AcctPeriod (
    @Embedded val trans: Trans,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "PayerID"
    )
    val period: List<Period>
)

data class AcctRecipient (
    @Embedded val trans: Trans,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "RecipientID"
    )
    val period: List<Period>
)
