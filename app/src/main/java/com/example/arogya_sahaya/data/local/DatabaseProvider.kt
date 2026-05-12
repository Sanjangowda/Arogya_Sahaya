package com.example.arogya_sahaya.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun init(context: Context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "arogya_db"
            ).build()
        }
    }

    fun getDatabase(): AppDatabase {
        return INSTANCE
            ?: throw IllegalStateException("DatabaseProvider not initialized")
    }
}