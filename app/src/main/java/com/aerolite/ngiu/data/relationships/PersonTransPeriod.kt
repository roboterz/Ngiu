package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Period
import com.aerolite.ngiu.data.entities.Person
import com.aerolite.ngiu.data.entities.Trans


data class PersonTrans (
    @Embedded val person: Person,
    @Relation(
        parentColumn = "Person_ID",
        entityColumn = "Person_ID"
    )
    val trans: List<Trans>
)

data class PersonPeriod (
    @Embedded val person: Person,
    @Relation(
        parentColumn = "Person_ID",
        entityColumn = "Period_Person_ID",
    )
    val period: List<Period>
)
