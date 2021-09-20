package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["indiv_id"], unique = true)])
data class Individual (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indiv_id")
    val id: Int,
    @ColumnInfo(name = "indiv_name")
    val Name: String
)