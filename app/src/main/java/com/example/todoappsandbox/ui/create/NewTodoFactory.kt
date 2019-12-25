package com.example.todoappsandbox.ui.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoDB

class NewTodoFactory(application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    private val todoDB = TodoDB.getInstance(application.applicationContext)
    private val dao = todoDB.todoDao()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewTodoViewModel(TodoRepository(dao)) as T
    }
}
