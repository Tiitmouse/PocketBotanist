package hr.algebra.lorena.pocketbotanist.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.algebra.lorena.pocketbotanist.model.Notification
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository
import hr.algebra.lorena.pocketbotanist.utils.NotificationHelper

class WateringReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val plantId = inputData.getInt("plantId", -1)
        if (plantId == -1) {
            return Result.failure()
        }

        val repository = PlantRepository(applicationContext)
        val plant = repository.getPlantById(plantId) ?: return Result.failure()

        val message = "It's time to water your ${plant.name}!"

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()
        notificationHelper.sendNotification(plant.id, plant.name, message)

        repository.insertNotification(
            Notification(
                id = 0,
                plantId = plant.id,
                message = message,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
        )
        repository.updateLastWateredTimestamp(plant.id, System.currentTimeMillis())

        return Result.success()
    }
}