package hu.pk.passworthy.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hu.pk.passworthy.R
import hu.pk.passworthy.dialogfragment.ChangePasswordDialogFragment
import hu.pk.passworthy.dialogfragment.EnterDaysUntilNotificationDialogFragment

class SettingsFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val sP = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        val editor = sP.edit()

        val btnChangeMPW = preferenceManager.findPreference<Preference>("masterpw")
        btnChangeMPW?.setOnPreferenceClickListener {
            ChangePasswordDialogFragment().show(
                requireActivity().supportFragmentManager,
                ChangePasswordDialogFragment.TAG
            )
            true
        }

        val btnEnterDaysUntil = preferenceManager.findPreference<Preference>("daysuntilalert")
        btnEnterDaysUntil?.setOnPreferenceClickListener {
            EnterDaysUntilNotificationDialogFragment().show(
                requireActivity().supportFragmentManager,
                EnterDaysUntilNotificationDialogFragment.TAG
            )
            true
        }
    }
}