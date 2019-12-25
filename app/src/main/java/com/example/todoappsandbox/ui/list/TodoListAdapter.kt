package com.example.todoappsandbox.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.databinding.TodoItemBinding
import com.example.todoappsandbox.repository.db.TodoEntity
import java.util.Locale

class TodoListAdapter(todoTouchEvent: TodoTouchEvent, val viewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var todos: List<TodoEntity> = arrayListOf()
    /**
     * Use this in filtering on searchView
     * This is basically used as a copy of [todos]
     */
    private var filteredTodos: List<TodoEntity> = arrayListOf()

    private val events = todoTouchEvent
    private var isChecked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = filteredTodos.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = filteredTodos[position]
        holder.binding.todo = entity
        holder.binding.event = events
        holder.binding.viewModel = viewModel
    }

    /**
     * Update todoLists for liveData
     * */
    fun setAllTodos(todoItems: List<TodoEntity>) {
        todos = todoItems
        filteredTodos = todoItems
        notifyDataSetChanged()
    }

    fun setIsChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    @Suppress("UNCHECKED_CAST")
    fun filtering() = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            return FilterResults().apply {
                values = filteringTodosByQuery(p0)
            }
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            filteredTodos = p1?.values as? List<TodoEntity> ?: arrayListOf()
            notifyDataSetChanged()
        }
    }

    private fun filteringTodosByQuery(query: CharSequence?): List<TodoEntity> {
        filteredTodos = if (query.isNullOrEmpty() || query.isNullOrBlank()) {
            todos
        } else {
            todos.filter {
                it.title.toLowerCase(Locale.US).contains(query.toString().toLowerCase(Locale.US)) || it.description.toLowerCase(
                    Locale.US
                ).contains(query.toString().toLowerCase(Locale.US))
            }
        }
        return filteredTodos
    }

    class ViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Touch event callbacks for activity
     */
    interface TodoTouchEvent {
        fun onDeleteClicked(entity: TodoEntity)
        fun onTodoClicked(entity: TodoEntity)
        fun onCheckClicked(entity: TodoEntity)
    }
}
