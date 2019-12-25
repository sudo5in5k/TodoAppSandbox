package com.example.todoappsandbox.repository.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(entity: TodoEntity)

    @Delete
    suspend fun delete(entity: TodoEntity)

    @Update
    suspend fun update(entity: TodoEntity)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    suspend fun getAll(): List<TodoEntity>
}
