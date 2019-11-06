package com.example.todoappsandbox.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.container.*

class TodoActivity : AppCompatActivity(),
    TodoListAdapter.TodoEvents {

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TodoListAdapter(this)
        todo_list_recycler.layoutManager = LinearLayoutManager(this)
        todo_list_recycler.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            TodoListFactory(this.application)
        ).get(TodoViewModel::class.java)
        viewModel.loadAllTodos()
        viewModel.allTodos.observe(this, Observer {
            adapter.setAllTodos(it)
        })

        fab.setOnClickListener {
            val intent = Intent(this, NewTodoActivity::class.java)
            startActivityForResult(intent, Consts.INTENT_FROM_FAB)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onDeleteClicked(entity: TodoEntity) {
        viewModel.deleteTodo(entity)
        viewModel.loadAllTodos()
    }

    override fun onTodoClicked(entity: TodoEntity) {
        val intent =
            Intent(this, NewTodoActivity::class.java).also { it.putExtra(Consts.INTENT, entity) }
        startActivityForResult(intent, Consts.INTENT_FROM_VIEW)
    }

}
