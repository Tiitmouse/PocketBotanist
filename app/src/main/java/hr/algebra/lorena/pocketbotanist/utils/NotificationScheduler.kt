package hr.algebra.lorena.pocketbotanist.utils

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.worker.SunlightReminderWorker
import hr.algebra.lorena.pocketbotanist.worker.WateringReminderWorker
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        private const val WATERING_WORK_TAG_PREFIX = "watering_work_"
        private const val SUNLIGHT_WORK_TAG_PREFIX = "sunlight_work_"
    }

    fun scheduleNotifications(plant: Plant) {
        cancelNotifications(plant.id)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val waterReminderEnabled = sharedPreferences.getBoolean("water_reminder", true)
        val sunlightReminderEnabled = sharedPreferences.getBoolean("sunlight_reminder", true)

        if (!plant.notificationsEnabled) {
            return
        }

        val data = Data.Builder()
            .putInt("plantId", plant.id)
            .build()

        if (waterReminderEnabled) {
            val wateringRequest = PeriodicWorkRequestBuilder<WateringReminderWorker>(
                plant.wateringFrequencyDays.toLong(), TimeUnit.DAYS
            )
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WATERING_WORK_TAG_PREFIX + plant.id,
                ExistingPeriodicWorkPolicy.UPDATE,
                wateringRequest
            )
        }

        if (sunlightReminderEnabled) {
            val sunlightRequest = PeriodicWorkRequestBuilder<SunlightReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                SUNLIGHT_WORK_TAG_PREFIX + plant.id,
                ExistingPeriodicWorkPolicy.UPDATE,
                sunlightRequest
            )
        }
    }

    fun cancelNotifications(plantId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork(WATERING_WORK_TAG_PREFIX + plantId)
        WorkManager.getInstance(context).cancelUniqueWork(SUNLIGHT_WORK_TAG_PREFIX + plantId)
    }
}