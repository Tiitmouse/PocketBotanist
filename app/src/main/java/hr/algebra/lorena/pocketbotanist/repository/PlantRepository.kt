package hr.algebra.lorena.pocketbotanist.repository

import android.content.ContentValues
import android.content.Context
import hr.algebra.lorena.pocketbotanist.dal.*
import hr.algebra.lorena.pocketbotanist.model.Notification
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantRepository(private val context: Context) {

    private val dbHelper = PocketBotanistSqlHelper(context)

    // ContentProvider fake example
    fun getAllPlantsFromProvider(): List<Plant> {
        val plants = mutableListOf<Plant>()
        val cursor = context.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                plants.add(
                    Plant(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                        latinName = it.getString(it.getColumnIndexOrThrow(COLUMN_LATIN_NAME)),
                        description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        wateringFrequencyDays = it.getInt(it.getColumnIndexOrThrow(COLUMN_WATERING_FREQUENCY)),
                        sunlightPreference = it.getString(it.getColumnIndexOrThrow(COLUMN_SUNLIGHT_PREFERENCE)),
                        imageUrl = it.getString(it.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        notificationsEnabled = it.getInt(it.getColumnIndexOrThrow(COLUMN_NOTIFICATIONS_ENABLED)) == 1,
                        lastWateredTimestamp = it.getLong(it.getColumnIndexOrThrow(COLUMN_LAST_WATERED_TIMESTAMP))
                    )
                )
            }
        }
        return plants
    }

    fun getAllPlants(): List<Plant> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_PLANTS, null, null, null, null, null, null)
        val plants = mutableListOf<Plant>()

        while (cursor.moveToNext()) {
            plants.add(
                Plant(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    latinName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATIN_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    wateringFrequencyDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WATERING_FREQUENCY)),
                    sunlightPreference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUNLIGHT_PREFERENCE)),
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                    notificationsEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATIONS_ENABLED)) == 1,
                    lastWateredTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_WATERED_TIMESTAMP))
                )
            )
        }
        cursor.close()
        return plants
    }

    fun insertPlant(plant: Plant): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, plant.name)
            put(COLUMN_LATIN_NAME, plant.latinName)
            put(COLUMN_DESCRIPTION, plant.description)
            put(COLUMN_WATERING_FREQUENCY, plant.wateringFrequencyDays)
            put(COLUMN_SUNLIGHT_PREFERENCE, plant.sunlightPreference)
            put(COLUMN_IMAGE_URL, plant.imageUrl)
            put(COLUMN_NOTIFICATIONS_ENABLED, if (plant.notificationsEnabled) 1 else 0)
            put(COLUMN_LAST_WATERED_TIMESTAMP, plant.lastWateredTimestamp)
        }
        return db.insert(TABLE_PLANTS, null, values)
    }

    fun getPlantById(id: Int): Plant? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_PLANTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var plant: Plant? = null
        if (cursor.moveToFirst()) {
            plant = Plant(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                latinName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATIN_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                wateringFrequencyDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WATERING_FREQUENCY)),
                sunlightPreference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUNLIGHT_PREFERENCE)),
                imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                notificationsEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATIONS_ENABLED)) == 1,
                lastWateredTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_WATERED_TIMESTAMP))
            )
        }
        cursor.close()
        return plant
    }

    fun updatePlant(plant: Plant): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, plant.name)
            put(COLUMN_LATIN_NAME, plant.latinName)
            put(COLUMN_DESCRIPTION, plant.description)
            put(COLUMN_WATERING_FREQUENCY, plant.wateringFrequencyDays)
            put(COLUMN_SUNLIGHT_PREFERENCE, plant.sunlightPreference)
            put(COLUMN_IMAGE_URL, plant.imageUrl)
            put(COLUMN_NOTIFICATIONS_ENABLED, if (plant.notificationsEnabled) 1 else 0)
            put(COLUMN_LAST_WATERED_TIMESTAMP, plant.lastWateredTimestamp)
        }
        return db.update(
            TABLE_PLANTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(plant.id.toString())
        )
    }

    fun deletePlant(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            TABLE_PLANTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun insertNotification(notification: Notification): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTIFICATION_PLANT_ID, notification.plantId)
            put(COLUMN_NOTIFICATION_MESSAGE, notification.message)
            put(COLUMN_NOTIFICATION_TIMESTAMP, notification.timestamp)
            put(COLUMN_NOTIFICATION_IS_READ, if (notification.isRead) 1 else 0)
        }
        return db.insert(TABLE_NOTIFICATIONS, null, values)
    }

    fun getAllNotifications(): List<Notification> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_NOTIFICATIONS, null, null, null, null, null, "$COLUMN_NOTIFICATION_TIMESTAMP DESC")
        val notifications = mutableListOf<Notification>()

        while (cursor.moveToNext()) {
            notifications.add(
                Notification(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_ID)),
                    plantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_PLANT_ID)),
                    message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_MESSAGE)),
                    timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_TIMESTAMP)),
                    isRead = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_IS_READ)) == 1
                )
            )
        }
        cursor.close()
        return notifications
    }

    fun getUnreadNotificationCount(): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NOTIFICATIONS WHERE $COLUMN_NOTIFICATION_IS_READ = 0", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun markAllNotificationsAsRead(): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTIFICATION_IS_READ, 1)
        }
        return db.update(TABLE_NOTIFICATIONS, values, "$COLUMN_NOTIFICATION_IS_READ = 0", null)
    }

    fun updateLastWateredTimestamp(plantId: Int, timestamp: Long): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LAST_WATERED_TIMESTAMP, timestamp)
        }
        return db.update(
            TABLE_PLANTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(plantId.toString())
        )
    }

    fun deleteAllNotifications(): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_NOTIFICATIONS, null, null)
    }
}