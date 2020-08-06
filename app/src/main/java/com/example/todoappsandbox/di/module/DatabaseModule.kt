package com.example.todoappsandbox.di.module

import android.content.Context
import com.example.todoappsandbox.data.repository.db.TodoDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDb(context: Context) = TodoDB.getInstance(context)

    @JvmStatic
    @Singleton
    @Provides
    fun provideDao(db: TodoDB) = db.todoDao()
}