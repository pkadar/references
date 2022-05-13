package hu.pk.passworthy.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import hu.pk.passworthy.R
import hu.pk.passworthy.databinding.FragmentEnterDaysUntilNotificationDialogBinding
import java.util.concurrent.TimeUnit


class EnterDaysUntilNotificationDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentEnterDaysUntilNotificationDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentEnterDaysUntilNotificationDialogBinding.inflate(
            LayoutInflater.from(requireContext())
        )

        val sP = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        binding.enterDays.editText?.setText(TimeUnit.MILLISECONDS.toDays(sP.getLong("daysuntilalert", 7776000000)).toString())

        return AlertDialog.Builder(requireContext())
            .setTitle(null)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.save_text)) { dialogInterface, i ->
                if (binding.enterDays.editText?.text.toString().isNotEmpty()) {
                    val number : Int? = binding.enterDays.editText?.text.toString().toIntOrNull()
                    if(number != null) {
                        if(number >= 0) {
                            val editor = sP.edit()
                            editor.putLong(
                                "daysuntilalert",
                                TimeUnit.DAYS.toMillis(
                                    binding.enterDays.editText?.text.toString().toLong()
                                )
                            )
                            editor.apply()
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.number_of_days_changed),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_negative_numbers),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.must_be_integer),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else Toast.makeText(
                    requireContext(),
                    getString(R.string.can_not_be_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.cancel_text), null)
            .create()
    }

    companion object {
        const val TAG = "EnterDaysUntilNotificationDialogFragment"
    }
}