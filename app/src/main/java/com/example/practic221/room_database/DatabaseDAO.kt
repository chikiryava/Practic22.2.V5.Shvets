package com.example.practic221.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practic221.entities.ConversionsEntity
import com.example.practic221.entities.UserdataEntity


@Dao
interface DatabaseDAO {
    @Insert
    suspend fun insertUser(user: UserdataEntity)

    @Query("SELECT * FROM user_data WHERE login = :login")
    suspend fun findUser(login: String): UserdataEntity

    @Insert
    suspend fun insertConversion(conversion:ConversionsEntity)

    @Query("SELECT * FROM conversions")
    suspend fun getAllConversions():List<ConversionsEntity>

    @Query("SELECT * FROM user_data")
    suspend fun getAllUsers():List<UserdataEntity>
    @Delete
    suspend fun deleteUser(user:UserdataEntity)
    @Delete
    suspend fun deleteConversion(conversion: ConversionsEntity)
    @Update
    suspend fun updateUser(user:UserdataEntity)
    @Update
    suspend fun updateConversions(conversion: ConversionsEntity)
}