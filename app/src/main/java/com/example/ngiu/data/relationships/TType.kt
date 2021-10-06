package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.*

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
    val mainCategories: List<MainCategory>
)

data class TransTypePeriod (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "TransactionType_ID",
        entityColumn = "Period_TransactionType_ID"
    )
    val period: List<Period>
)
