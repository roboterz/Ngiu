package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Merchant
import com.aerolite.ngiu.data.entities.Period

data class MerchantPeriod (
    @Embedded val merchant: Merchant,
    @Relation(
        parentColumn = "Merchant_ID",
        entityColumn = "Period_Merchant_ID"
    )
    val period: List<Period>
)
