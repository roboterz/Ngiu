package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.Period
import com.aerolite.ngiu.data.entities.Trans

data class AcctTransRecipient (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "Account_ID",
        entityColumn = "AccountRecipient_ID"
    )
    val trans: List<Trans>
)

data class AcctTransAcct (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "Account_ID",
        entityColumn = "Account_ID"
    )
    val trans: List<Trans>
)


data class AcctPeriodRecipient (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "Account_ID",
        entityColumn = "Period_AccountRecipient_ID"
    )
    val period: List<Period>
)

data class AcctPeriodAcct (
    @Embedded val account: Account,
    @Relation(
        parentColumn = "Account_ID",
        entityColumn = "Period_Account_ID"
    )
    val period: List<Period>
)
