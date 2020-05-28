package com.example.todoappsandbox.di.module

import com.example.todoappsandbox.data.repository.TodoRepository
import com.example.todoappsandbox.data.repository.db.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MainModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideTodoRepository(dao: TodoDao) = TodoRepository(dao)
}