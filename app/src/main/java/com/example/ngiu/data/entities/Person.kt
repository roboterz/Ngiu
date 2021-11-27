package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Person_ID"], unique = true)])
data class Person (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    val Person_ID: Long,
    //@ColumnInfo(name = "name")
    val Person_Name: String
)