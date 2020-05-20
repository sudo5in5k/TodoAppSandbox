package com.example.todoappsandbox.ui.list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.R
import com.example.todoappsandbox.data.ResponseResult
import com.example.todoappsandbox.databinding.ActivityMainBinding
import com.example.todoappsandbox.data.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts
import kotlinx.android.synthetic.main.todo_item.view.*

class TodoActivity : AppCompatActivity(), TodoListAdapter.TodoTouchEvent {

    private val todoViewModel: TodoViewModel by viewModels { TodoListFactory(this.application) }
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
                onDeleteClicked(entity)
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
                        drawIcon(c, it, dX)
                        drawBackGround(c, it, dX, viewHolder.adapterPosition)
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
                is ResponseResult.Success -> {
                    todoListAdapter.setAllTodos(it.data)
                }
                else -> Unit
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
            R.mipmap.ic_delete_on_swipe_round,
            itemView.context.theme
        )
        icon.bounds = iconBounds
        icon.draw(canvas)
    }

    /**
     * set background on icon
     */
    private fun drawBackGround(canvas: Canvas, itemView: View, dx: Float, pos: Int) {
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val width = itemView.right - itemView.left
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val marginFromIconToItemView = itemView.resources.getDimensionPixelSize(R.dimen.icon_margin)

        // change alpha depending on distance (dX)
        val colorRGB = Color.GRAY
        bgPaint.color = Color.argb(
            ALPHA_MAX_VALUE - (dx * ALPHA_MAX_VALUE / width).toInt(),
            colorRGB.red,
            colorRGB.green,
            colorRGB.blue
        )

        canvas.drawCircle(
            itemView.right + dx + marginFromIconToItemView + iconSize / 2,
            (itemView.bottom - itemView.top) / 2f + itemView.height * pos,
            iconSize * RADIUS_RATIO,
            bgPaint
        )
    }

    companion object {
        private const val SWIPE_THRESHOLD = 0.5f
        private const val ALPHA_MAX_VALUE = 255
        private const val RADIUS_RATIO = 0.8f
    }
}
