package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Trans

data class PeriodTrans (
    @Embedded val period: Period,
    @Relation(
        parentColumn = "ID",
        entityColumn = "PeriodID"
    )
    val trans: List<Trans>
)

data class AcctPeriod (
    @Embedded val trans: Trans,
    @Relation(
        parentColumn = "ID",
        entityColumn = "PayerID"
    )
    val period: List<Period>
)

data class AcctRecipient (
    @Embedded val trans: Trans,
    @Relation(
        parentColumn = "ID",
        entityColumn = "RecipientID"
    )
    val period: List<Period>
)
