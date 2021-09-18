package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Reimburse
import com.example.ngiu.data.entities.Transaction

data class ReimbTrans (
    @Embedded val reimburse: Reimburse,
    @Relation(
        parentColumn = "reimburse_id",
        entityColumn = "ReimburseID"
    )
    val transaction: Transaction
)

data class TransReimb (
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "trans_id",
        entityColumn = "TransactionID"
    )
    val reimburse: Reimburse
)