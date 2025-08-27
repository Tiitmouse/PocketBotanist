package hr.algebra.lorena.pocketbotanist.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import androidx.preference.PreferenceManager
import java.util.Locale

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {
        fun updateLocale(context: Context): ContextWrapper {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val language = sharedPreferences.getString("language", "en") ?: "en"
            val locale = Locale(language)
            Locale.setDefault(locale)

            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)

            val updatedContext = context.createConfigurationContext(configuration)
            return ContextUtils(updatedContext)
        }
    }
}