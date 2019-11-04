package com.example.todoappsandbox.ui.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity

class NewTodoViewModel(application: Application) : AndroidViewModel(application) {

    val activityTitle = MutableLiveData<String>()
    val todoTitle = MutableLiveData<String>()
    val todoDescription = MutableLiveData<String>()

    val repository = TodoRepository(application)

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
        activityTitle.value = if (todoEntity != null) "編集" else "新規"
    }

}