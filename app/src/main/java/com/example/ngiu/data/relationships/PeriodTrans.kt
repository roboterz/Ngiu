package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Trans

data class PeriodTrans (
    @Embedded val period: Period,
    @Relation(
        parentColumn = "Period_ID",
        entityColumn = "Period_ID"
    )
    val trans: List<Trans>
)
