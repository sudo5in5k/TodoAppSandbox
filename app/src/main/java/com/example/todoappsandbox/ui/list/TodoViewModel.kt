package com.example.todoappsandbox.ui.list

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts

class TodoViewModel(val repository: TodoRepository) : ViewModel() {

    val allTodos = MutableLiveData<List<TodoEntity>>()
    val entity = MutableLiveData<TodoEntity?>()
    val topVisibility = MutableLiveData<Boolean>()

    init {
        loadAllTodos()
    }

    private fun insertTodo(entity: TodoEntity) {
        repository.insertTodo(entity)
    }

    private fun updateTodo(entity: TodoEntity) {
        repository.updateTodo(entity)
        loadAllTodos()
    }

    fun deleteTodo(entity: TodoEntity) {
        repository.deleteTodo(entity)
        loadAllTodos()
    }

    fun checkTodo(entity: TodoEntity) {
        val nowChecked = !entity.isChecked
        updateTodo(TodoEntity(entity.id, entity.title, entity.description, nowChecked))
    }

    private fun loadAllTodos() {
        allTodos.postValue(repository.getAllTodos())
    }

    fun setEntity(entity: TodoEntity) {
        this.entity.postValue(entity)
    }

    fun switchVisibilityByTodos() {
        if (allTodos.value.isNullOrEmpty()) {
            topVisibility.postValue(true)
        } else {
            topVisibility.postValue(false)
        }
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
