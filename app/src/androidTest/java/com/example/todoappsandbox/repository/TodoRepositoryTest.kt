package com.example.todoappsandbox.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoappsandbox.repository.db.TodoDB
import com.example.todoappsandbox.repository.db.TodoDao
import com.example.todoappsandbox.repository.db.TodoEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoRepositoryTest {

    lateinit var db: TodoDB
    lateinit var dao: TodoDao
    lateinit var repository: TodoRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDB::class.java).build()
        dao = db.todoDao()
        repository = TodoRepository(dao)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun isRepositoryCached() {
        val testEntity = TodoEntity(id = null, title = "hoge", description = "huga")
        repository.insertTodo(testEntity)
        assert(repository.getAllTodos().isNotEmpty())

        val insertEntity = TodoEntity(id = 2, title = "huga", description = "piyo")
        repository.insertTodo(insertEntity)

        assert(repository.getAllTodos().size == 2)
    }
}