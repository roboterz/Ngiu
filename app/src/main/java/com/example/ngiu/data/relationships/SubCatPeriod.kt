package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.*

data class SubCatPeriod (
    @Embedded val subCategories: SubCategory,
    @Relation(
        parentColumn = "SubCategory_ID",
        entityColumn = "Period_SubCategory_ID"
    )
    val trans: List<Period>
)
