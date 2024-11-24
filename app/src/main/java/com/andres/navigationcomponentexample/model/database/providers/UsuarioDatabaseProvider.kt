package com.andres.navigationcomponentexample.model.database.providers

import android.content.Context
import androidx.room.Room
import com.andres.navigationcomponentexample.UsuarioDatabase


object UsuarioDatabaseProvider {
    fun getDatabase(context: Context): UsuarioDatabase {
        return Room.databaseBuilder(
            context,
            UsuarioDatabase::class.java,
            "app_database"
        ).build()
    }
}