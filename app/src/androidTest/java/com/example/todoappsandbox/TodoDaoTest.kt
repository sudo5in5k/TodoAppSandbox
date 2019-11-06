package com.example.todoappsandbox

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoappsandbox.repository.db.TodoDB
import com.example.todoappsandbox.repository.db.TodoDao
import com.example.todoappsandbox.repository.db.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {

    lateinit var db: TodoDB
    lateinit var dao: TodoDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDB::class.java).build()
        dao = db.todoDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        db.close()
    }

    @Test
    fun verifyInsert() {
        val entity = TodoEntity(id = null, title = "hoge", description = "")
        runBlocking {
            launch(Dispatchers.IO) {
                dao.insert(entity)
                val data = dao.getAll()
                assertEquals(entity.title, data[0].title)
            }
        }
    }

    @Test
    fun verifyDelete() {
        val entity = TodoEntity(id = null, title = "hoge", description = "")
        val deleteEntity = TodoEntity(id = 1, title = "hoge", description = "")
        runBlocking {
            launch(Dispatchers.IO) {
                dao.insert(entity)
                dao.delete(deleteEntity)
                val data = dao.getAll()
                assertEquals(true, data.isEmpty())
            }
        }
    }

    @Test
    fun verifyUpdate() {
        val entity = TodoEntity(id = null, title = "hoge", description = "")
        val updateEntity = TodoEntity(id = 1, title = "huga", description = "foo")
        runBlocking {
            launch(Dispatchers.IO) {
                dao.insert(entity)
                dao.update(updateEntity)
                val data = dao.getAll()
                assertEquals(updateEntity.title, data[0].title)
            }
        }
    }
}