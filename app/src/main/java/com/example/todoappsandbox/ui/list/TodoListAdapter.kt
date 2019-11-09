package com.example.todoappsandbox.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.databinding.TodoItemBinding
import com.example.todoappsandbox.repository.db.TodoEntity

class TodoListAdapter(todoCrudEvent: TodoCrudEvent, val viewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var todoList: List<TodoEntity> = arrayListOf()
    private val events = todoCrudEvent
    private var isChecked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = todoList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = todoList[position]
        holder.binding.todo = entity
        holder.binding.event = events
        holder.binding.viewModel = viewModel
    }

    /**
     * Update todoLists for liveData
     * */
    fun setAllTodos(todoItems: List<TodoEntity>) {
        todoList = todoItems
        notifyDataSetChanged()
    }

    fun setIsChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    class ViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Define touch event callbacks for activity
     */
    interface TodoCrudEvent {
        fun onDeleteClicked(entity: TodoEntity)
        fun onTodoClicked(entity: TodoEntity)
    }
}