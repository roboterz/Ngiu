package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Account
import com.example.ngiu.data.entities.AccountType

data class AcctTypeAcct (
    @Embedded val accountType: AccountType,
    @Relation(
        parentColumn = "ID",
        entityColumn = "AcctTypeID"
    )
    val account: List<Account>
)
