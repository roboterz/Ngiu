package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "projectId")
    val id: Int,
    @ColumnInfo(name = "periodName")
    val Name: String
)