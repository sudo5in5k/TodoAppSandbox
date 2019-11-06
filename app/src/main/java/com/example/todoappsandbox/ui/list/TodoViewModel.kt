package com.example.todoappsandbox.ui.list

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TodoRepository = TodoRepository(application)
    val allTodos = MutableLiveData<List<TodoEntity>>()

    private fun insertTodo(entity: TodoEntity) {
        repository.insertTodo(entity)
    }

    private fun updateTodo(entity: TodoEntity) {
        repository.updateTodo(entity)
    }

    fun deleteTodo(entity: TodoEntity) {
        repository.deleteTodo(entity)
    }

    fun loadAllTodos() {
        allTodos.postValue(repository.getAllTodos())
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val entity = data?.getParcelableExtra<TodoEntity>(Consts.INTENT) ?: return
            when (requestCode) {
                Consts.INTENT_FROM_FAB -> insertTodo(entity)
                Consts.INTENT_FROM_VIEW -> updateTodo(entity)
            }
            loadAllTodos()
        }
    }
}