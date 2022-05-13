package hu.pk.passworthy.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import hu.pk.passworthy.R
import hu.pk.passworthy.data.ItemDatabase
import hu.pk.passworthy.databinding.FragmentChangePasswordDialogBinding
import hu.pk.passworthy.encryption.Encryption
import kotlin.concurrent.thread


class ChangePasswordDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentChangePasswordDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentChangePasswordDialogBinding.inflate(LayoutInflater.from(requireContext()))

        return AlertDialog.Builder(requireContext())
            .setTitle(null)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save_text)) {
                    dialogInterface, i ->
                if(binding.changeMPWInput.editText?.text.toString().isNotEmpty()){
                    if(binding.changeMPWInput.editText?.text.toString().length > 5) {
                        val sP =
                            PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
                        val editor = sP.edit()
                        editor.putString(
                            "masterpw",
                            Encryption.toSHA256(binding.changeMPWInput.editText?.text.toString())
                        )
                        editor.apply()


                        val tmpdb = ItemDatabase.getDatabase(requireContext())
                        thread {
                            val tmpitems = tmpdb.itemDao().getAll()
                            Encryption.decryptAllItems(tmpitems)
                            Encryption.initEncDecKey(binding.changeMPWInput.editText?.text.toString())
                            for(item in tmpitems){
                                tmpdb.itemDao().update(Encryption.encryptItem(item))
                            }
                        }


                        Toast.makeText(
                            requireContext(),
                            getString(R.string.mp_changed_text),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.password_min_length), Toast.LENGTH_LONG).show()
                    }
                } else Toast.makeText(requireContext(), getString(R.string.password_required_text), Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.cancel_text), null)
            .create()
    }

    companion object {
        const val TAG = "ChangePasswordDialogFragment"
    }
}