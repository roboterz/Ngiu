package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Reimburse
import com.example.ngiu.data.entities.SubCategories
import com.example.ngiu.data.entities.Transaction

data class MainCatSub (
    @Embedded val mainCatSub: MainCatSub,
    @Relation(
        parentColumn = "main_cat_id",
        entityColumn = "MainCategoryID"
    )
    val subCategories: List<SubCategories>
)
