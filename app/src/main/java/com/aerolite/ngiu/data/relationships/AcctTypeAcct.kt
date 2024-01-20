package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.AccountType

data class AcctTypeAcct (
    @Embedded val accountType: AccountType,
    @Relation(
        parentColumn = "AccountType_ID",
        entityColumn = "AccountType_ID"
    )
    val account: List<Account>
)
