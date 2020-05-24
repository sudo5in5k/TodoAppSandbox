package com.example.todoappsandbox.di

import android.content.Context
import com.example.todoappsandbox.data.repository.db.TodoDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDb(context: Context) = TodoDB.getInstance(context)

    @Singleton
    @Provides
    fun provideDao(db: TodoDB) = db.todoDao()
}