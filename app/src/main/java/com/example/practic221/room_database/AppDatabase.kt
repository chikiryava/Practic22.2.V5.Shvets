package com.example.practic221.room_database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.practic221.entities.ConversionsEntity
import com.example.practic221.entities.UserdataEntity

@Database(entities = [UserdataEntity::class, ConversionsEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "MyConverterDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}