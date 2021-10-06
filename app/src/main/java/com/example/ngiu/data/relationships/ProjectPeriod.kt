package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Project

data class ProjectPeriod (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "Project_ID",
        entityColumn = "Period_Project_ID"
    )
    val period: List<Period>
)
