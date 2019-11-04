package com.example.todoappsandbox.ui.create

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts
import kotlinx.android.synthetic.main.activity_new_todo.*

class NewTodoActivity : AppCompatActivity() {

    private var todoEntity: TodoEntity? = null
    lateinit var viewModel: NewTodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_todo)

        viewModel = ViewModelProvider(
            this,
            NewTodoFactory(this.application)
        ).get(NewTodoViewModel::class.java)

        if (intent != null && intent.hasExtra(Consts.INTENT)) {
            val entity = intent.getParcelableExtra<TodoEntity>(Consts.INTENT)
            todoEntity = entity

            viewModel.setTodo(entity)
            viewModel.todoDescription.observe(this, Observer {
                register_description.text = Editable.Factory.getInstance().newEditable(it)
            })
            viewModel.todoTitle.observe(this, Observer {
                register_title.text = Editable.Factory.getInstance().newEditable(it)
            })
        }

        viewModel.setMenuTitle(todoEntity)
        viewModel.activityTitle.observe(this, Observer {
            title = it
        })
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