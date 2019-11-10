package com.example.todoappsandbox.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoappsandbox.R
import com.example.todoappsandbox.databinding.ActivityMainBinding
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts

class TodoActivity : AppCompatActivity(),
    TodoListAdapter.TodoCrudEvent {

    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoListAdapter

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            TodoListFactory(this.application)
        ).get(TodoViewModel::class.java)

        adapter = TodoListAdapter(this, viewModel)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.lifecycleOwner = this

        activityMainBinding.also {
            it.activity = this@TodoActivity
            it.viewModel = viewModel
        }

        activityMainBinding.todoListRecycler.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
        }

        viewModel.loadAllTodos()
        viewModel.allTodos.observe(this, Observer {
            adapter.setAllTodos(it)
        })

        viewModel.isCheckedState.observe(this, Observer {
            adapter.setIsChecked(it)
        })
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

    fun onFabClicked() {
        val intent = Intent(this, NewTodoActivity::class.java)
        startActivityForResult(intent, Consts.INTENT_FROM_FAB)
    }

}
