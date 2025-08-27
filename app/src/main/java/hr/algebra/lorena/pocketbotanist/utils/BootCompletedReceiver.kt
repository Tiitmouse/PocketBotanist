package hr.algebra.lorena.pocketbotanist.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository
import hr.algebra.lorena.pocketbotanist.utils.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val repository = PlantRepository(context)
            val scheduler = NotificationScheduler(context)

            CoroutineScope(Dispatchers.IO).launch {
                val plants = repository.getAllPlants()
                plants.forEach { plant ->
                    scheduler.scheduleNotifications(plant)
                }
            }
        }
    }
}