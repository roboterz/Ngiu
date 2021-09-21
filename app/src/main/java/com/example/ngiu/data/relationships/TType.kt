package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.MainCategories
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.TransactionType
import com.example.ngiu.data.entities.Trans

data class TransTypeTrans (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "trans_type_id",
        entityColumn = "TypeID"
    )
    val trans: List<Trans>
)

data class TransTypeMC (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "trans_type_id",
        entityColumn = "TypeID"
    )
    val mainCategories: List<MainCategories>
)

data class TransTypePeriod (
    @Embedded val transactionType: TransactionType,
    @Relation(
        parentColumn = "trans_type_id",
        entityColumn = "TypeID"
    )
    val period: List<Period>
)
