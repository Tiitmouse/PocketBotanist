package hr.algebra.lorena.pocketbotanist.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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

// DODAJTE OVU FUNKCIJU
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}