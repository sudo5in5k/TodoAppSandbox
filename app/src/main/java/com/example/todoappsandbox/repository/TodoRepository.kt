package com.example.todoappsandbox.repository

import com.example.todoappsandbox.repository.db.TodoDao
import com.example.todoappsandbox.repository.db.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository(private val todoDao: TodoDao) {

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

    fun getAllTodos(): List<TodoEntity> {
        var allTodos: List<TodoEntity> = listOf()
        runBlocking {
            launch(Dispatchers.IO) {
                allTodos = todoDao.getAll()
            }
        }
        return allTodos
    }
}