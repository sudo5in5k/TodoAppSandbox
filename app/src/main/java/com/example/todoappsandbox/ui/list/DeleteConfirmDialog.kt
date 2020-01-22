package com.example.todoappsandbox.ui.list

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.todoappsandbox.repository.db.TodoEntity

class DeleteConfirmDialog : DialogFragment() {

    var onPositiveListener: DialogInterface.OnClickListener? = null
    var onNegativeListener: DialogInterface.OnClickListener? = null
    var entity: TodoEntity? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) dismiss()
        val activity = this.activity
        activity ?: return super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(activity)
            .setTitle("Delete")
            .setMessage(
                "Are you sure to delete this todo?\n\nTitle: ${entity?.title
                    ?: "Nothing"}\nDescription: ${entity?.description ?: "Nothing"}"
            )
            .setPositiveButton("OK", onPositiveListener)
            .setNegativeButton("CANCEL", onNegativeListener)
        this.isCancelable = false
        return builder.create()
    }

    companion object {
        fun newInstance() = DeleteConfirmDialog()
        fun newInstance(entity: TodoEntity) = DeleteConfirmDialog().apply {
            this.entity = entity
        }
    }
}
