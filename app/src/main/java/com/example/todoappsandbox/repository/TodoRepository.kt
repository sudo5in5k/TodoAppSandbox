package com.example.todoappsandbox.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.todoappsandbox.repository.db.TodoDB
import com.example.todoappsandbox.repository.db.TodoDao
import com.example.todoappsandbox.repository.db.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository(application: Application) {

    private val todoDao: TodoDao
    private val allTodos: LiveData<List<TodoEntity>>

    init {
        val db = TodoDB.getInstance(application.applicationContext)
        todoDao = db.todoDao()
        allTodos = todoDao.getAll()
    }

    fun insertTodo(entity: TodoEntity) = runBlocking {
        launch(Dispatchers.IO) {
            todoDao.insert(entity)
        }
    }

    fun updateTodo(entity: TodoEntity) = runBlocking {
        launch(Dispatchers.IO) {
            todoDao.update(entity)
        }
    }

    fun deleteTodo(entity: TodoEntity) = runBlocking {
        launch(Dispatchers.IO) {
            todoDao.delete(entity)
        }
    }

    fun getAllTodos(): LiveData<List<TodoEntity>> {
        return allTodos
    }
}