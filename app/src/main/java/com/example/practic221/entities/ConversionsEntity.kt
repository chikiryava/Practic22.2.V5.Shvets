package com.example.practic221.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "conversions")
data class ConversionsEntity(
    @PrimaryKey(autoGenerate = true) val id:Long,
    val originalCurrency:String,
    val finalCurrency:String,
    val amountCurrency:Double,
    val conversionResult:Double,
    val conversionTime: Date
)