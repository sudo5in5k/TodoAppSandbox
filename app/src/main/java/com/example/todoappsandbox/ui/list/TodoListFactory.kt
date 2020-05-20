package com.example.todoappsandbox.ui.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsandbox.data.repository.TodoRepository
import com.example.todoappsandbox.data.repository.db.TodoDB

class TodoListFactory(application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    private val todoDB = TodoDB.getInstance(application.applicationContext)
    private val dao = todoDB.todoDao()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(TodoRepository(dao)) as T
    }
}
