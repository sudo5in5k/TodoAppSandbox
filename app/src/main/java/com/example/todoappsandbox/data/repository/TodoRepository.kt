package com.example.todoappsandbox.data.repository

import com.example.todoappsandbox.data.repository.db.TodoDao
import com.example.todoappsandbox.data.repository.db.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

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

    fun getAllUnchecked(): List<TodoEntity> {
        var uncheckedTodos: List<TodoEntity> = listOf()
        runBlocking {
            launch(Dispatchers.IO) {
                uncheckedTodos = todoDao.getUnchecked()
            }
        }
        return uncheckedTodos
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
