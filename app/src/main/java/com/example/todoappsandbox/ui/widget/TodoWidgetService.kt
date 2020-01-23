package com.example.todoappsandbox.ui.widget

import android.app.Application
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.todoappsandbox.R
import com.example.todoappsandbox.repository.TodoRepository
import com.example.todoappsandbox.repository.db.TodoDB
import com.example.todoappsandbox.repository.db.TodoEntity

class TodoWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return TodoWidgetFactory(application)
    }

    private class TodoWidgetFactory(private val application: Application) : RemoteViewsFactory {
        private val todoDB = TodoDB.getInstance(application.applicationContext)
        private val dao = todoDB.todoDao()
        private val repository = TodoRepository(dao)
        private var uncheckedTodos: List<TodoEntity> = listOf()

        override fun onCreate() {
            return
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun onDataSetChanged() {
            uncheckedTodos = repository.getAllUnchecked()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getViewAt(p0: Int): RemoteViews {
            return RemoteViews(application.packageName, R.layout.todo_unchecked_item).apply {
                setTextViewText(R.id.todo_title, uncheckedTodos[p0].title)
                setOnClickFillInIntent(R.id.unchecked_todo_container, Intent())
            }
        }

        override fun getCount(): Int {
            return uncheckedTodos.count()
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun onDestroy() {
            return
        }
    }
}
