package com.aerolite.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.aerolite.ngiu.data.entities.Category
import com.aerolite.ngiu.data.entities.Period

data class CategoryPeriod (
    @Embedded val categories: Category,
    @Relation(
        parentColumn = "Category_ID",
        entityColumn = "Period_Category_ID"
    )
    val trans: List<Period>
)
