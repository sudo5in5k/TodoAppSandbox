package com.example.todoappsandbox.ui.list

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.todoappsandbox.data.repository.db.TodoEntity

class DeleteConfirmDialog : DialogFragment() {

    var onPositiveListener: (() -> Unit)? = null
    var onNegativeListener: (() -> Unit)? = null

    private lateinit var entity: TodoEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entity =
            arguments?.getParcelable(KEY_ENTITY) ?: throw NullPointerException("Entity is null")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) dismiss()
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage(
                "Are you sure to delete this todo?\n\nTitle: ${entity.title}\nDescription: ${entity.description}"
            )
            .setPositiveButton("OK") { _, _ ->
                onPositiveListener?.invoke()
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
                onNegativeListener?.invoke()
            }
        isCancelable = false
        return builder.create()
    }

    companion object {
        private const val KEY_ENTITY = "entity"
        fun newInstance(entity: TodoEntity) = DeleteConfirmDialog().apply {
            val bundle = Bundle()
            bundle.putParcelable(KEY_ENTITY, entity)
            arguments = bundle
        }
    }
}
