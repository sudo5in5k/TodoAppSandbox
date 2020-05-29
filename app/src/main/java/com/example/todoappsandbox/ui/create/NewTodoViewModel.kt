package com.example.todoappsandbox.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.data.repository.db.TodoEntity

class NewTodoViewModel(val entity: TodoEntity?) : ViewModel() {

    val todoTitle = MutableLiveData<String>()
    val todoDescription = MutableLiveData<String>()

    private val _backToList = MutableLiveData<TodoEntity>()
    val backToList: LiveData<TodoEntity>
        get() = _backToList

    init {
        if (entity != null) {
            todoTitle.value = entity.title
            todoDescription.value = entity.description
        }
    }

    fun saveTodo() {
        val currentTodoEntity = TodoEntity(
            entity?.id,
            todoTitle.value ?: "",
            todoDescription.value ?: "",
            entity?.isChecked ?: false
        )
        _backToList.value = currentTodoEntity
    }
}
