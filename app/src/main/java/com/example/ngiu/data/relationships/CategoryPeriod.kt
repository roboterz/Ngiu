package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.*

data class CategoryPeriod (
    @Embedded val categories: Category,
    @Relation(
        parentColumn = "Category_ID",
        entityColumn = "Period_Category_ID"
    )
    val trans: List<Period>
)
