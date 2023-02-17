package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.MainCategory
import com.example.ngiu.data.entities.SubCategory

data class MainCatSub (
    @Embedded val mainCategories: MainCategory,
    @Relation(
        parentColumn = "MainCategory_ID",
        entityColumn = "MainCategory_ID"
    )
    val subCategories: List<SubCategory>
)
