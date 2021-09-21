package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Period
import com.example.ngiu.data.entities.Project

data class ProjectPeriod (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "project_id",
        entityColumn = "ProjectID"
    )
    val period: List<Period>
)
