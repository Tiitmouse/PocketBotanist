package hr.algebra.lorena.pocketbotanist.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference = findPreference<SwitchPreferenceCompat>("dark_theme")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isChecked = newValue as Boolean
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            true
        }

        val languagePreference = findPreference<ListPreference>("language")
        languagePreference?.setOnPreferenceChangeListener { _, newValue ->
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
            true
        }

        val onboardingPreference = findPreference<SwitchPreferenceCompat>("show_onboarding")
        onboardingPreference?.setOnPreferenceChangeListener { _, newValue ->
            val show = newValue as Boolean
            val sharedPref = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                if (show) {
                    remove("finished")
                } else {
                    putBoolean("finished", true)
                }
                apply()
            }
            true
        }

        val refreshDataPreference = findPreference<Preference>("refresh_data")
        refreshDataPreference?.setOnPreferenceClickListener {
            showRefreshConfirmationDialog()
            true
        }

        viewModel.refreshStatus.observe(this) { status ->
            Toast.makeText(context, status, Toast.LENGTH_LONG).show()
        }
    }

    private fun showRefreshConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.refresh_dialog_title))
            .setMessage(getString(R.string.refresh_dialog_message))
            .setPositiveButton(getString(R.string.delete_button)) { _, _ ->
                viewModel.refreshData()
            }
            .setNegativeButton(getString(R.string.cancel_button), null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideFab()
    }
}