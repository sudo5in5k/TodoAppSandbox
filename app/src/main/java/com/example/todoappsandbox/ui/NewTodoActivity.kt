package com.example.todoappsandbox.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts
import kotlinx.android.synthetic.main.activity_new_todo.*

class NewTodoActivity : AppCompatActivity() {

    private var todoEntity: TodoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_todo)

        val intent = intent
        if (intent != null && intent.hasExtra(Consts.INTENT)) {
            val entity = intent.getParcelableExtra<TodoEntity>(Consts.INTENT)
            todoEntity = entity
            register_title.text = entity?.title as Editable
            register_description.text = entity.description as Editable
        }
        title = if (todoEntity != null) "編集" else "新規"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> saveTodo()
            else -> Unit
        }
        return true
    }

    private fun saveTodo() {
        val id = if (todoEntity != null) todoEntity?.id else null
        val todo = TodoEntity(id, "${register_title.text}", "${register_description.text}")
        val intent = Intent()
        intent.putExtra(Consts.INTENT, todo)
        setResult(RESULT_OK, intent)
        finish()
    }

}