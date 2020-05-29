package com.example.todoappsandbox.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsandbox.data.repository.db.TodoEntity

class NewTodoFactory(private val entity: TodoEntity?) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewTodoViewModel(entity) as T
    }
}
