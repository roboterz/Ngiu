package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["Budget_ID"], unique = true)])
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var Budget_ID: Long = 0L,
    var Budget_Type: Int = 0,
    var Category_ID: Long = 0L,
    var Budget_Amount: Double = 0.00,
    var Budget_UploadStatus:Boolean = false
)
