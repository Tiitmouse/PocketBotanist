package hr.algebra.lorena.pocketbotanist.utils

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import hr.algebra.lorena.pocketbotanist.model.Plant
import hr.algebra.lorena.pocketbotanist.worker.SunlightReminderWorker
import hr.algebra.lorena.pocketbotanist.worker.WateringReminderWorker
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    companion object {
        private const val WATERING_WORK_TAG_PREFIX = "watering_work_"
        private const val SUNLIGHT_WORK_TAG_PREFIX = "sunlight_work_"
        private const val MIN_PERIODIC_INTERVAL_MINUTES = 15L
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
            // FOR TESTING: Treat days as minutes
            val wateringFrequencyMinutes = plant.wateringFrequencyDays.toLong()
            val nextWateringTime = plant.lastWateredTimestamp + TimeUnit.MINUTES.toMillis(wateringFrequencyMinutes)
            val initialDelay = nextWateringTime - System.currentTimeMillis()

            if (initialDelay > 0) {
                val wateringRequest = OneTimeWorkRequestBuilder<WateringReminderWorker>()
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()

                WorkManager.getInstance(context).enqueueUniqueWork(
                    WATERING_WORK_TAG_PREFIX + plant.id,
                    ExistingWorkPolicy.REPLACE,
                    wateringRequest
                )
            }
        }

        if (sunlightReminderEnabled) {
            // FOR TESTING: Schedule for every 15 minutes (the minimum allowed)
            val sunlightRequest = OneTimeWorkRequestBuilder<SunlightReminderWorker>()
                .setInitialDelay(MIN_PERIODIC_INTERVAL_MINUTES, TimeUnit.MINUTES)
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                SUNLIGHT_WORK_TAG_PREFIX + plant.id,
                ExistingWorkPolicy.REPLACE,
                sunlightRequest
            )
        }
    }

    fun cancelNotifications(plantId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork(WATERING_WORK_TAG_PREFIX + plantId)
        WorkManager.getInstance(context).cancelUniqueWork(SUNLIGHT_WORK_TAG_PREFIX + plantId)
    }
}