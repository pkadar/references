package hu.pk.passworthy.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import hu.pk.passworthy.R
import hu.pk.passworthy.data.Item
import hu.pk.passworthy.data.ItemDatabase
import kotlin.concurrent.thread

class DeleteItemConfirmationDialogFragment(var fragment: Fragment, var receivedItem: Item): DialogFragment() {
    interface DeleteItemListener {
        fun onDeleteItemListener(itemToDelete:Item)
    }

    private lateinit var listener: DeleteItemListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = fragment as? DeleteItemConfirmationDialogFragment.DeleteItemListener ?: throw RuntimeException("Activity must implement the DeleteItemConfirmationDialogFragment interface!")
    }

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
     //   val title = "Are you sure you want to delete item named: ${receivedItem.name}?"
        val title = getString(R.string.delete_one_item_confirmation_text, receivedItem.name)
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(getString(R.string.yes_text), DialogInterface.OnClickListener { dialogInterface, i ->
                listener.onDeleteItemListener(receivedItem)
            })
            .setNegativeButton(getString(R.string.cancel_text), null)
            .create()
    }

    companion object {
        const val TAG = "DeleteItemConfirmationDialogFragment"
    }
}