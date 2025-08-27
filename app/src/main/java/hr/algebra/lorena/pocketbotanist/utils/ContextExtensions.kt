package hr.algebra.lorena.pocketbotanist.utils

import android.content.Context
import android.content.SharedPreferences

private const val ONBOARDING_PREFS = "onboarding"
private const val FINISHED_KEY = "finished"

fun Context.getOnboardingPrefs(): SharedPreferences =
    getSharedPreferences(ONBOARDING_PREFS, Context.MODE_PRIVATE)

fun SharedPreferences.Editor.setOnboardingFinished(finished: Boolean) {
    putBoolean(FINISHED_KEY, finished)
    apply()
}

fun SharedPreferences.isOnboardingFinished(): Boolean =
    getBoolean(FINISHED_KEY, false)