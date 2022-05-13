package hu.pk.passworthy.dialogfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import hu.pk.passworthy.R
import hu.pk.passworthy.data.Item
import hu.pk.passworthy.databinding.DialogAddNewItemBinding

class AddNewItemDialogFragment(var fragment: Fragment, val receivedItem: Item?) : DialogFragment() {
    interface AddNewItemDialogListener {
        fun onItemCreated(newItem: Item)
        fun onItemEdited(editedItem: Item)
    }

    private lateinit var listener: AddNewItemDialogListener
    private lateinit var binding: DialogAddNewItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = fragment as? AddNewItemDialogListener ?: throw RuntimeException("Activity must implement the AddNewItemDialogFragment interface!")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddNewItemBinding.inflate(LayoutInflater.from(context))

        val clipboard : ClipboardManager = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        binding.btnUserToClipboard.setOnClickListener {
            val clipData = ClipData.newPlainText(getString(R.string.username_text), binding.usernameField.editText?.text.toString())
            clipboard.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), getString(R.string.username_copied_clipboard_text), Toast.LENGTH_SHORT).show()
        }
        binding.btnPwToClipboard.setOnClickListener {
            val clipData = ClipData.newPlainText(getString(R.string.password_text), binding.passwordField.editText?.text.toString())
            clipboard.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), getString(R.string.password_copied_clipboard_text), Toast.LENGTH_SHORT).show()
        }

        binding.descriptionField.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if((motionEvent.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.extraInfoField.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if((motionEvent.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        loadItem()

        val title = if(receivedItem == null) getString(R.string.add_new_item_text) else getString(R.string.edit_existing_item_text)
        val okbutton = if(receivedItem == null) getString(R.string.add_text) else getString(R.string.edit_text)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(binding.root)
            .setPositiveButton(okbutton) {
                dialogInterface, i ->
                if(isValid()){
                    when(receivedItem) {
                        null -> listener.onItemCreated(getItem())
                        else -> listener.onItemEdited(getItem())
                    }
                } else Toast.makeText(requireContext(), getString(R.string.name_required_text), Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.cancel_text), null)
            .create()
    }

    private fun isValid() = binding.nameField.editText?.text.toString().isNotEmpty()

    private fun getItem(): Item {
        if (receivedItem == null) {
            return Item(
                null,
                binding.nameField.editText?.text.toString(),
                binding.descriptionField.text.toString(),
                binding.usernameField.editText?.text.toString(),
                binding.passwordField.editText?.text.toString(),
                System.currentTimeMillis(),
                binding.extraInfoField.text.toString()
            )
        } else {
            if(!receivedItem.password.equals(binding.passwordField.editText?.text.toString())) {
                receivedItem.lastModified = System.currentTimeMillis()
            }
            receivedItem.name = binding.nameField.editText?.text.toString()
            receivedItem.description = binding.descriptionField.text.toString()
            receivedItem.username = binding.usernameField.editText?.text.toString()
            receivedItem.password = binding.passwordField.editText?.text.toString()
            receivedItem.extraInfo = binding.extraInfoField.text.toString()
            return receivedItem
        }
    }

    private fun loadItem(){
        if(receivedItem != null) {
            binding.nameField.editText?.setText(receivedItem.name)
            binding.descriptionField.setText(receivedItem.description)
            binding.usernameField.editText?.setText(receivedItem.username)
            binding.passwordField.editText?.setText(receivedItem.password)
            binding.extraInfoField.setText(receivedItem.extraInfo)
        }
    }
    companion object {
        const val TAG = "AddNewItemDialogFragment"
    }
}