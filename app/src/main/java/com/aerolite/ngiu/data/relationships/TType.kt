package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Category
import com.aerolite.ngiu.data.entities.Period
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.data.entities.TransactionType

data class TransTypeTrans (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "TransactionType_ID",
        entityColumn = "TransactionType_ID"
    )
    val trans: List<Trans>
)

data class TransTypeMC (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "TransactionType_ID",
        entityColumn = "TransactionType_ID"
    )
    val categories: List<Category>
)

data class TransTypePeriod (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "TransactionType_ID",
        entityColumn = "Period_TransactionType_ID"
    )
    val period: List<Period>
)
