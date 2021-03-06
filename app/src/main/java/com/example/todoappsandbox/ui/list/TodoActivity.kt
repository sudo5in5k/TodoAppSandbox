package com.example.todoappsandbox.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.R
import com.example.todoappsandbox.data.State
import com.example.todoappsandbox.data.repository.db.TodoEntity
import com.example.todoappsandbox.databinding.ActivityMainBinding
import com.example.todoappsandbox.di.ViewModelFactory
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.todo_item.view.*
import javax.inject.Inject

class TodoActivity : DaggerAppCompatActivity(), TodoListAdapter.TodoTouchEvent {

    @Inject
    lateinit var todoViewModelFactory: ViewModelFactory
    lateinit var todoViewModel: TodoViewModel
    private lateinit var todoListAdapter: TodoListAdapter
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
                todoViewModel.deleteTodo(entity)
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return SWIPE_THRESHOLD
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder?.itemView?.container?.setBackgroundColor(Color.LTGRAY)
                }
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.container.setBackgroundColor(Color.WHITE)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    viewHolder.itemView.also {
                        drawBackGround(c, it, dX)
                        drawIcon(c, it, dX)
                    }
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                } else {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        0f,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        }

    private val itemTouchHelper = ItemTouchHelper(swipeToDismissCallBack)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(this, todoViewModelFactory).get(TodoViewModel::class.java)
        todoListAdapter = TodoListAdapter(this, todoViewModel)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.apply {
            viewModel = todoViewModel
            lifecycleOwner = this@TodoActivity
        }

        activityMainBinding.todoListRecycler.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(this@TodoActivity)
            setHasFixedSize(true)
            itemTouchHelper.attachToRecyclerView(this)
            addItemDecoration(
                DividerItemDecoration(
                    this@TodoActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        todoViewModel.result.observe(this, Observer {
            when (it) {
                is State.Loading -> activityMainBinding.progress.visibility = View.VISIBLE
                is State.Error -> {
                    activityMainBinding.progress.visibility = View.GONE
                    activityMainBinding.defaultTop.visibility = View.VISIBLE
                }
                is State.Success -> {
                    activityMainBinding.progress.visibility = View.GONE
                    if (it.data.isNullOrEmpty()) {
                        activityMainBinding.defaultTop.visibility = View.VISIBLE
                    } else {
                        activityMainBinding.defaultTop.visibility = View.GONE
                    }
                    todoListAdapter.setAllTodos(it.data)
                }
            }
        })

        todoViewModel.toDeleteDialog.observe(this, Observer {
            val deleteConfirmDialog = DeleteConfirmDialog.newInstance(it).apply {
                onPositiveListener = {
                    todoViewModel.deleteTodo(it)
                }
                onNegativeListener = {
                    Toast.makeText(this@TodoActivity, "Delete cancelled", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            deleteConfirmDialog.show(supportFragmentManager, null)
        })

        todoViewModel.toNew.observe(this, Observer {
            if (it) {
                searchView.onActionViewCollapsed()
                val intent = Intent(this, NewTodoActivity::class.java)
                startActivityForResult(intent, Consts.INTENT_FROM_FAB)
                overridePendingTransition(R.anim.normal, R.anim.bottom_up)
            }
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
                    todoListAdapter.filtering().filter(p0)
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    todoListAdapter.filtering().filter(p0)
                    return false
                }
            })
        }
        return true
    }

    override fun onDeleteClicked(entity: TodoEntity) {
        searchView.onActionViewCollapsed()
        todoViewModel.toDeleteDialog(entity)
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

    private fun drawIcon(canvas: Canvas, itemView: View, dx: Float) {
        val iconBounds = Rect()
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val marginFromIconToItemView = itemView.resources.getDimensionPixelSize(R.dimen.icon_margin)
        val marginVertical = (itemView.bottom - itemView.top - iconSize) / 2
        with(iconBounds) {
            left = itemView.right + dx.toInt() + marginFromIconToItemView
            top = itemView.top + marginVertical
            right = itemView.right + dx.toInt() + marginFromIconToItemView + iconSize
            bottom = itemView.bottom - marginVertical
        }

        val icon = itemView.resources.getDrawable(
            R.drawable.ic_archive_gray_24dp,
            itemView.context.theme
        )
        icon.bounds = iconBounds
        icon.draw(canvas)
    }

    /**
     * set background on icon
     */
    private fun drawBackGround(canvas: Canvas, itemView: View, dx: Float) {
        val colorRGB = Color.parseColor(BACKGROUND_COLOR_DELETE)
        val background = ColorDrawable().apply {
            color = colorRGB
            setBounds(itemView.right + dx.toInt(), itemView.top, itemView.right, itemView.bottom)
        }
        background.draw(canvas)
    }

    companion object {
        private const val SWIPE_THRESHOLD = 0.5f
        private const val BACKGROUND_COLOR_DELETE = "#f44336"
    }
}
