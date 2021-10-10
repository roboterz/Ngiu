package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Merchant
import com.example.ngiu.data.entities.Period

data class MerchantPeriod (
    @Embedded val merchant: Merchant,
    @Relation(
        parentColumn = "Merchant_ID",
        entityColumn = "Period_Merchant_ID"
    )
    val period: List<Period>
)
