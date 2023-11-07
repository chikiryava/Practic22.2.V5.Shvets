package com.example.practic221.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserdataEntity(
    @PrimaryKey
    val login: String,
    val password: String
)
