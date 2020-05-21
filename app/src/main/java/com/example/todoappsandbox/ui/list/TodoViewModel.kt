package com.example.todoappsandbox.ui.list

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.data.State
import com.example.todoappsandbox.data.repository.TodoRepository
import com.example.todoappsandbox.data.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts
import com.example.todoappsandbox.utils.SingleLiveEvent

class TodoViewModel(val repository: TodoRepository) : ViewModel() {

    private val _toDeleteDialog = SingleLiveEvent<TodoEntity>()
    val toDeleteDialog: LiveData<TodoEntity>
        get() = _toDeleteDialog

    private val _result = MutableLiveData<State<TodoEntity>>()
    val result: LiveData<State<TodoEntity>>
        get() = _result

    private val _toNew = MutableLiveData<Boolean>()
    val toNew: LiveData<Boolean>
        get() = _toNew

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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun loadAllTodos() {
        try {
            _result.value = State.Loading
            val todos = repository.getAllTodos()
            if (todos.isNullOrEmpty()) {
                _result.value = State.Success(emptyList())
            } else {
                _result.value = State.Success(todos)
            }
        } catch (e: Exception) {
            _result.value = State.Error(e)
        }
    }

    fun toDeleteDialog(entity: TodoEntity) {
        _toDeleteDialog.postValue(entity)
    }

    fun onFabClicked() {
        _toNew.value = true
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
