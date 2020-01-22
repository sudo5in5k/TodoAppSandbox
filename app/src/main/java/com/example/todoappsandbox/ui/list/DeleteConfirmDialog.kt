package com.example.todoappsandbox.ui.list

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DeleteConfirmDialog : DialogFragment() {

    var onPositiveListener: DialogInterface.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = this.activity
        activity ?: return super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(activity)
            .setTitle("Delete")
            .setMessage("This todo was deleted")
            .setPositiveButton("OK", onPositiveListener)
            .setNegativeButton("CANCEL", null)
        return builder.create()
    }

    companion object {
        fun newInstance() = DeleteConfirmDialog()
    }
}
