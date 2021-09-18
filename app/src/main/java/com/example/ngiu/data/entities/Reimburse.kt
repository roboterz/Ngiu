package com.example.ngiu.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Transaction::class,
        parentColumns = arrayOf("trans_id"),
        childColumns = arrayOf("TransactionID"),
        onDelete = ForeignKey.SET_NULL,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class Reimburse (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reimburse_id")
    val id: Int,
    val TransactionID: Int,
    val Status: Boolean
)