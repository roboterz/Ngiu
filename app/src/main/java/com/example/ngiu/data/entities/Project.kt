package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Project_ID"], unique = true)])
data class Project (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Project_ID: Long,
    //@ColumnInfo(name = "Name")
    var Project_Name: String
)