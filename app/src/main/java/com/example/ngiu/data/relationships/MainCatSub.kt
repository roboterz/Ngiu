package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.MainCategories
import com.example.ngiu.data.entities.SubCategories

data class MainCatSub (
    @Embedded val mainCategories: MainCategories,
    @Relation(
        parentColumn = "ID",
        entityColumn = "MainCategoryID"
    )
    val subCategories: List<SubCategories>
)
