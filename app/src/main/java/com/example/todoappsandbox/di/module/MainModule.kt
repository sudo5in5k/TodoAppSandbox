package com.example.todoappsandbox.di.module

import com.example.todoappsandbox.data.repository.TodoRepository
import com.example.todoappsandbox.data.repository.db.TodoDao
import dagger.Module
import dagger.Provides

@Module
object MainModule {

    @JvmStatic
    @Provides
    fun provideTodoRepository(dao: TodoDao) = TodoRepository(dao)
}