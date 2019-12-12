package com.example.todoappsandbox.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoappsandbox.R
import com.example.todoappsandbox.databinding.ActivityMainBinding
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts

class TodoActivity : AppCompatActivity(),
    TodoListAdapter.TodoTouchEvent {

    private val viewModel: TodoViewModel by viewModels { TodoListFactory(this.application) }
    private lateinit var adapter: TodoListAdapter

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchMenu = menu?.findItem(R.id.action_search)

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as? SearchManager ?: return false

        val searchView = searchMenu?.actionView as? SearchView ?: return false
        searchView.apply {
            maxWidth = Int.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    adapter.filtering().filter(p0)
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adapter.filtering().filter(p0)
                    return false
                }
            })
        }
        return true
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

    override fun onCheckClicked(entity: TodoEntity) {
        viewModel.checkTodo(entity)
    }

    fun onFabClicked() {
        val intent = Intent(this, NewTodoActivity::class.java)
        startActivityForResult(intent, Consts.INTENT_FROM_FAB)
    }

}
