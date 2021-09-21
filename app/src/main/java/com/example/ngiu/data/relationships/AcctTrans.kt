package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Trans

data class AcctTransRecipient (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "RecipientID"
    )
    val trans: List<Trans>
)

data class AcctTransPayer (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "PayerID"
    )
    val trans: List<Trans>
)


data class AcctPeriodRecipient (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "RecipientID"
    )
    val period: List<Period>
)

data class AcctPeriodPayer (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "acct_id",
        entityColumn = "PayerID"
    )
    val period: List<Period>
)
