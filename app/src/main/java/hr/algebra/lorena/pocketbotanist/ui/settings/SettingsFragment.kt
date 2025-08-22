package hr.algebra.lorena.pocketbotanist.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import hr.algebra.lorena.pocketbotanist.R

class SettingsFragment : PreferenceFragmentCompat() {

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
    }
}