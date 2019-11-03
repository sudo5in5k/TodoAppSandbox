package com.example.todoappsandbox.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(entity: TodoEntity)

    @Delete
    suspend fun delete(entity: TodoEntity)

    @Update
    suspend fun update(entity: TodoEntity)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    fun getAll(): LiveData<List<TodoEntity>>
}