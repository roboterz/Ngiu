package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Person_ID"], unique = true)])
data class Person (
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var Person_ID: Long,
    //@ColumnInfo(name = "name")
    var Person_Name: String
)