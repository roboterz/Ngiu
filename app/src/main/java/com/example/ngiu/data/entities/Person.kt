package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["person_id"], unique = true)])
data class Person (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    val id: Int,
    @ColumnInfo(name = "person_name")
    val Name: String
)