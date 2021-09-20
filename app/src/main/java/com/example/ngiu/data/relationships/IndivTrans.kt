package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Individual
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Transaction

data class IndivTrans (
    @Embedded val individual: Individual,
    @Relation(
        parentColumn = "indiv_id",
        entityColumn = "IndividualID"
    )
    val transaction: List<Transaction>
)

data class IndivPeriod (
    @Embedded val individual: Individual,
    @Relation(
        parentColumn = "indiv_id",
        entityColumn = "IndividualID",
    )
    val transaction: List<Period>
)