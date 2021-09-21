package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Project
import com.example.ngiu.data.entities.SubCategories
import com.example.ngiu.data.entities.Trans

data class SubCatPeriod (
    @Embedded val subCategories: SubCategories,
    @Relation(
        parentColumn = "sub_cat_id",
        entityColumn = "CategoryID"
    )
    val trans: List<Period>
)
