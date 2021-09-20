package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Individual
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Transaction

data class PersonTrans (
    @Embedded val individual: Individual,
    @Relation(
        parentColumn = "person_id",
        entityColumn = "PersonID"
    )
    val transaction: List<Transaction>
)

data class PersonPeriod (
    @Embedded val individual: Individual,
    @Relation(
        parentColumn = "person_id",
        entityColumn = "PersonID",
    )
    val transaction: List<Period>
)