package com.example.notepad

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteNoteDialogFragment(var notetitle : String) : DialogFragment() {
    interface MyDialogListener{
        fun onPositiveClick()
        fun onNegativeCLick()
    }

    var listener : MyDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete '$notetitle'")
        builder.setMessage("Are you sure you want to permanently delete that note ?")
            .setPositiveButton("Yes", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    listener?.onPositiveClick()
                }
            })
            .setNegativeButton("No", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    listener?.onNegativeCLick()
                }
            })
        return builder.create()
    }
}