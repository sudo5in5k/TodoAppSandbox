package com.example.todoappsandbox.ui.widget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity

class TodoWidgetViewModel(val repository: TodoRepository): ViewModel() {

    val uncheckedTodos = MutableLiveData<List<TodoEntity>>()

    fun loadUncheckedTodos() {
        uncheckedTodos.postValue(repository.getAllUnchecked())
    }
}