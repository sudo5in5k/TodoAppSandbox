package com.example.todoappsandbox.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity

class NewTodoViewModel(val repository: TodoRepository) : ViewModel() {

    val activityTitle = MutableLiveData<String>()
    val todoTitle = MutableLiveData<String>()
    val todoDescription = MutableLiveData<String>()

    private fun setTodoTitle(entity: TodoEntity?) {
        todoTitle.value = entity?.title
    }

    private fun setDescription(entity: TodoEntity?) {
        todoDescription.value = entity?.description
    }

    fun setTodo(entity: TodoEntity?) {
        setTodoTitle(entity)
        setDescription(entity)
    }

    fun setMenuTitle(todoEntity: TodoEntity?) {
        activityTitle.value = if (todoEntity != null) "Edit" else "New"
    }

}