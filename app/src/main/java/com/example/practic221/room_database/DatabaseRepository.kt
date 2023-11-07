package com.example.practic221.room_database

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.practic221.entities.ConversionsEntity
import com.example.practic221.entities.UserdataEntity
import com.example.practic221.room_database.AppDatabase

class DatabaseRepository(private val database: AppDatabase, private val context: Context) {

    // Метод для вставки нового пользователя
    suspend fun insertUser(user: UserdataEntity) {
        val existingUser = findUser(user.login)
        if (existingUser == null) {
            database.databaseDao().insertUser(user)
            showMessage("Вы зарегистрировались!")
        } else {
            showMessage("Такой пользователь уже существует")
        }
    }

    // Метод для поиска пользователя по логину
    suspend fun findUser(login: String): UserdataEntity? {
        return database.databaseDao().findUser(login)
    }

    // Метод для вставки конвертации
    suspend fun insertConversion(conversion:ConversionsEntity){
        database.databaseDao().insertConversion(conversion)
    }

    // Метод для возврата всех конвертации
    suspend fun getAllConversions():List<ConversionsEntity>{
        return database.databaseDao().getAllConversions()
    }

    suspend fun getAllUsers():List<UserdataEntity>{
        return database.databaseDao().getAllUsers()
    }

    suspend fun deleteUser(user:UserdataEntity){
        database.databaseDao().deleteUser(user)
    }

    suspend fun deleteConversion(conversion:ConversionsEntity){
        database.databaseDao().deleteConversion(conversion)
    }

    suspend fun updateUser(user:UserdataEntity){
        database.databaseDao().updateUser(user)
    }
    suspend fun updateConversion(conversion: ConversionsEntity){
        database.databaseDao().updateConversions(conversion)
    }

    private fun showMessage(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}