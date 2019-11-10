package com.example.todoappsandbox.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoEntity
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class TodoViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private val repository = mock<TodoRepository>()
    lateinit var viewModel: TodoViewModel

    @Before
    fun setup() {
        viewModel = TodoViewModel(repository)
    }

    @Test
    fun verifyLoadTodoFromRepository() {
        `when`(repository.getAllTodos()).thenReturn(listOf())
        viewModel.loadAllTodos()
        verify(repository).getAllTodos()
    }

    @Test
    fun verifyObserveTodoList() {
        val observer = viewModel.allTodos.testObserver()

        val testEntity = TodoEntity(null, "hoge", "piyo")
        `when`(repository.getAllTodos()).thenReturn(listOf(testEntity))
        
        viewModel.loadAllTodos()
        verify(observer).onChanged(listOf(testEntity))
    }

    @Test
    fun verifyObserveTodoChecked() {
        val checkedObserver = viewModel.isCheckedState.testObserver()

        val testEntity = TodoEntity(1, "hoge", "piyo", isChecked = false)
        `when`(repository.getAllTodos()).thenReturn(listOf(testEntity))

        viewModel.loadAllTodos()
        viewModel.checkTodo(testEntity)
        verify(checkedObserver).onChanged(true)
    }

    /**
     * make mock observer for liveData
     */
    private fun <T> LiveData<T>.testObserver(): Observer<T> {
        val observer = mock<Observer<T>>()
        this.observeForever(observer)
        return observer
    }
}