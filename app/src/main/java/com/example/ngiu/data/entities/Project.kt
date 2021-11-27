package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Project_ID"], unique = true)])
data class Project (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val Project_ID: Long,
    //@ColumnInfo(name = "Name")
    val Project_Name: String
)