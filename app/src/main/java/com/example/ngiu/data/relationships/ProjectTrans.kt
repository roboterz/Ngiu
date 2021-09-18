package com.example.ngiu.data.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ngiu.data.entities.Project
import com.example.ngiu.data.entities.Transaction

data class ProjectTrans (
    @Embedded val project: Project,
    @Relation(
        parentColumn = "project_id",
        entityColumn = "ProjectID"
    )
    val transaction: List<Transaction>
)