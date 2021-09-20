package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["project_id"], unique = true)])
data class Project (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "project_id")
    val id: Int,
    @ColumnInfo(name = "project_name")
    val Name: String
)