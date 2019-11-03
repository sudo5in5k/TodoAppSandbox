package com.example.todoappsandbox.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.db.TodoEntity
import kotlinx.android.synthetic.main.todo_item.view.*

class TodoListAdapter(todoEvents: TodoEvents) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var todoList: List<TodoEntity> = arrayListOf()
    private val listener = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoList[position], listener = listener)
    }

    /**
     * Activity uses this method to update todoList with the help of LiveData
     * */
    fun setAllTodos(todoItems: List<TodoEntity>) {
        todoList = todoItems
        notifyDataSetChanged()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(entity: TodoEntity, listener: TodoEvents) {
            itemView.title_text.text = entity.title
            itemView.description_text.text = entity.description
            itemView.delete_image.setOnClickListener {
                listener.onDeleteClicked(entity)
            }
            itemView.setOnClickListener {
                listener.onTodoClicked(entity)
            }
        }
    }

    /**
     * Define touch event callbacks
     */
    interface TodoEvents {
        fun onDeleteClicked(entity: TodoEntity)
        fun onTodoClicked(entity: TodoEntity)
    }
}