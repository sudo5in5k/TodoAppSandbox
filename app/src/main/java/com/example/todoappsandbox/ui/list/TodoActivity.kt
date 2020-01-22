package com.example.todoappsandbox.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.R
import com.example.todoappsandbox.databinding.ActivityMainBinding
import com.example.todoappsandbox.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts

class TodoActivity : AppCompatActivity(),
    TodoListAdapter.TodoTouchEvent {

    private val todoViewModel: TodoViewModel by viewModels { TodoListFactory(this.application) }
    private lateinit var adapter: TodoListAdapter
    private lateinit var searchView: SearchView
    private lateinit var activityMainBinding: ActivityMainBinding
    private val swipeToDismissCallBack =
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val castViewHolder = viewHolder as? TodoListAdapter.ViewHolder ?: return
                val entity = castViewHolder.binding.todo ?: return
                onDeleteClicked(entity)
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return SWIPE_THRESHOLD
            }
        }

    private val itemTouchHelper = ItemTouchHelper(swipeToDismissCallBack)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TodoListAdapter(this, todoViewModel)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.lifecycleOwner = this

        activityMainBinding.also {
            it.activity = this@TodoActivity
            it.viewModel = todoViewModel
        }

        activityMainBinding.todoListRecycler.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            itemTouchHelper.attachToRecyclerView(it)
        }

        todoViewModel.loadAllTodos()
        todoViewModel.allTodos.observe(this, Observer {
            adapter.setAllTodos(it)
            todoViewModel.switchVisibilityByTodos()
        })

        todoViewModel.isCheckedState.observe(this, Observer {
            adapter.setIsChecked(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        todoViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchMenu = menu?.findItem(R.id.action_search)

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as? SearchManager ?: return false

        searchView = searchMenu?.actionView as? SearchView ?: return false
        searchView.apply {
            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            isSubmitButtonEnabled = false
            maxWidth = Int.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    adapter.filtering().filter(p0)
                    clearFocus()
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
        searchView.onActionViewCollapsed()
        DeleteConfirmDialog.newInstance().apply {
            onPositiveListener = DialogInterface.OnClickListener { _, _ ->
                todoViewModel.deleteTodo(entity)
                todoViewModel.loadAllTodos()
            }
        }.show(supportFragmentManager, null)
    }

    override fun onTodoClicked(entity: TodoEntity) {
        searchView.onActionViewCollapsed()
        val intent =
            Intent(this, NewTodoActivity::class.java).also { it.putExtra(Consts.INTENT, entity) }
        startActivityForResult(intent, Consts.INTENT_FROM_VIEW)
    }

    override fun onCheckClicked(entity: TodoEntity) {
        searchView.onActionViewCollapsed()
        todoViewModel.checkTodo(entity)
    }

    fun onFabClicked() {
        searchView.onActionViewCollapsed()
        val intent = Intent(this, NewTodoActivity::class.java)
        startActivityForResult(intent, Consts.INTENT_FROM_FAB)
    }

    companion object {
        private const val SWIPE_THRESHOLD = 0.2f
    }
}
