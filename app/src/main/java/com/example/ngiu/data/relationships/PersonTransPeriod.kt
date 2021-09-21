package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Person
import com.example.ngiu.data.entities.Trans


data class PersonTrans (
    @Embedded val person: Person,
    @Relation(
        parentColumn = "person_id",
        entityColumn = "PersonID"
    )
    val trans: List<Trans>
)

data class PersonPeriod (
    @Embedded val person: Person,
    @Relation(
        parentColumn = "person_id",
        entityColumn = "PersonID",
    )
    val period: List<Period>
)
