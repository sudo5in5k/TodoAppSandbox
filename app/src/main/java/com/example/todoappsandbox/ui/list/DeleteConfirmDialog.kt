package com.example.todoappsandbox.ui.list

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class DeleteConfirmDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = this.activity
        activity ?: return super.onCreateDialog(savedInstanceState)
        val viewModel = ViewModelProvider(this, TodoListFactory(activity.application)).get(
            TodoViewModel::class.java
        )
        val builder = AlertDialog.Builder(activity)
            .setTitle("Delete")
            .setMessage("This todo was deleted")
            .setPositiveButton("YES") { _, _ ->
                viewModel.deleteConfirmYes.postValue(true)
            }
            .setNegativeButton("NO", null)
        return builder.create()
    }
}