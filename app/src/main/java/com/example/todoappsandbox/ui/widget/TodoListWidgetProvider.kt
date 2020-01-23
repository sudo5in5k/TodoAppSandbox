package com.example.todoappsandbox.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.example.todoappsandbox.R
import com.example.todoappsandbox.ui.list.TodoActivity

/**
 * Implementation of App Widget functionality.
 */
class TodoListWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        return
    }

    override fun onDisabled(context: Context) {
        return
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        when (intent?.action) {
            ACTION_ITEM_CLICK -> {
                context?.startActivity(Intent(context, TodoActivity::class.java))
            }
            ACTION_RELOAD_CLICK -> {
                val applicationContext = context?.applicationContext ?: return
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetComponentName =
                    ComponentName(applicationContext, TodoListWidgetProvider::class.java)
                val ids = appWidgetManager.getAppWidgetIds(appWidgetComponentName)
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.unchecked_todo_list)
                Toast.makeText(context, "Widget Updated!", Toast.LENGTH_SHORT).show()
            }
            else -> return
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        try {
            val remoteViews = RemoteViews(context.packageName, R.layout.todo_list_widget)
            // set for list item
            val listItemIntent = Intent(context, TodoWidgetService::class.java)
            remoteViews.apply {
                setRemoteAdapter(R.id.unchecked_todo_list, listItemIntent)
                setEmptyView(R.id.unchecked_todo_list, R.id.empty)
            }

            // set for list item click
            setItemClickedPendingIntent(context, remoteViews)

            // set for reload button click
            setReloadClickedPendingIntent(context, remoteViews)

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        } catch (e: Exception) {
            Log.d("debug", "Error Caused! ${e.message}")
        }
    }

    private fun setItemClickedPendingIntent(context: Context, remoteViews: RemoteViews) {
        val intent = Intent(context, TodoListWidgetProvider::class.java).apply {
            action = ACTION_ITEM_CLICK
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setPendingIntentTemplate(R.id.unchecked_todo_list, pendingIntent)
    }

    private fun setReloadClickedPendingIntent(
        context: Context,
        remoteViews: RemoteViews
    ) {
        val intent = Intent(context, TodoListWidgetProvider::class.java).apply {
            action = ACTION_RELOAD_CLICK
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setOnClickPendingIntent(R.id.reload, pendingIntent)
    }

    companion object {
        const val ACTION_ITEM_CLICK = "ACTION_ITEM_CLICK"
        const val ACTION_RELOAD_CLICK = "ACTION_RELOAD_CLICK"
    }
}
