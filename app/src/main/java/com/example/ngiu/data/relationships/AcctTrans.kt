package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Transaction

data class AcctTransRecipient (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "RecipientID"
    )
    val transaction: List<Transaction>
)

data class AcctTransPayer (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "PayerID"
    )
    val transaction: List<Transaction>
)
