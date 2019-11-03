package com.example.todoappsandbox.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.utils.Consts
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.container.*

class TodoActivity : AppCompatActivity(), TodoListAdapter.TodoEvents {

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TodoListAdapter(this)
        todo_list_recycler.layoutManager = LinearLayoutManager(this)
        todo_list_recycler.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        viewModel.getAllTodoList().observe(this, Observer {
            adapter.setAllTodos(it)
        })

        fab.setOnClickListener {
            val intent = Intent(this, NewTodoActivity::class.java)
            startActivityForResult(intent, Consts.INTENT_FROM_FAB)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val entity = data?.getParcelableExtra<TodoEntity>(Consts.INTENT) ?: return
            when (requestCode) {
                Consts.INTENT_FROM_FAB -> viewModel.insertTodo(entity)
                Consts.INTENT_FROM_VIEW -> Unit
            }
        }
    }

    override fun onDeleteClicked(entity: TodoEntity) {
        viewModel.deleteTodo(entity)
    }

    override fun onTodoClicked(entity: TodoEntity) {
        //val intent = Intent(this, )
    }

}
