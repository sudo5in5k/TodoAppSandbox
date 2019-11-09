package com.example.todoappsandbox.ui.list

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts

class TodoViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository: TodoRepository = TodoRepository(app)
    val allTodos = MutableLiveData<List<TodoEntity>>()
    val isCheckedState = MutableLiveData<Boolean>()

    private fun insertTodo(entity: TodoEntity) {
        repository.insertTodo(entity)
    }

    private fun updateTodo(entity: TodoEntity) {
        repository.updateTodo(entity)
    }

    fun deleteTodo(entity: TodoEntity) {
        repository.deleteTodo(entity)
    }

    fun checkTodo(entity: TodoEntity) {
        val nowChecked = !entity.isChecked
        isCheckedState.postValue(nowChecked)
        updateTodo(TodoEntity(entity.id, entity.title, entity.description, nowChecked))
        loadAllTodos()
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