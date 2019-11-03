package com.example.todoappsandbox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository = TodoRepository(application)
    private val allTodos: LiveData<List<TodoEntity>> = repository.getAllTodos()

    fun insertTodo(entity: TodoEntity) {
        repository.insertTodo(entity)
    }

    fun updateTodo(entity: TodoEntity) {
        repository.updateTodo(entity)
    }

    fun deleteTodo(entity: TodoEntity) {
        repository.deleteTodo(entity)
    }

    fun getAllTodoList(): LiveData<List<TodoEntity>> {
        return allTodos
    }

}