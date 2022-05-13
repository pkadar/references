package hu.pk.passworthy.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import hu.pk.passworthy.R

class DeleteAllConfirmationDialogFragment(var fragment: Fragment): DialogFragment() {
    interface DeleteAllItemListener{
        fun onAllItemDeleteConfirmed()
    }

    private lateinit var listener: DeleteAllItemListener

    override fun onAttach(context: Context){
        super.onAttach(context)
        listener = fragment as? DeleteAllConfirmationDialogFragment.DeleteAllItemListener ?: throw RuntimeException("Activity must implement the DeleteAllConfirmationDialogFragment interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_all_confirmation_text))
            .setPositiveButton(getString(R.string.yes_text), DialogInterface.OnClickListener { dialogInterface, i -> listener.onAllItemDeleteConfirmed() })
            .setNegativeButton(getString(R.string.cancel_text), null)
            .create()
    }

    companion object {
        const val TAG = "DeleteAllConfirmationDialogFragment"
    }
}