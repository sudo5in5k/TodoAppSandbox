package com.example.todoappsandbox.data.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class], version = 2, exportSchema = false)
abstract class TodoDB : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: TodoDB? = null

        fun getInstance(context: Context): TodoDB =
            instance ?: synchronized(this) {
                Room.databaseBuilder(context, TodoDB::class.java, "todo_db")
                    .fallbackToDestructiveMigration().build()
            }
    }
}
