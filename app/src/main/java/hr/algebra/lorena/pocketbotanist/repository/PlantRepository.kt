package hr.algebra.lorena.pocketbotanist.repository

import android.content.ContentValues
import android.content.Context
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_ID
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_IMAGE_URL
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_LATIN_NAME
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_NAME
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_DESCRIPTION
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_SUNLIGHT_PREFERENCE
import hr.algebra.lorena.pocketbotanist.dal.COLUMN_WATERING_FREQUENCY
import hr.algebra.lorena.pocketbotanist.dal.PocketBotanistSqlHelper
import hr.algebra.lorena.pocketbotanist.dal.TABLE_PLANTS
import hr.algebra.lorena.pocketbotanist.model.Plant

class PlantRepository(context: Context) {

    private val dbHelper = PocketBotanistSqlHelper(context)

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
                    imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
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
        }
        return db.insert(TABLE_PLANTS, null, values)
    }

    // TODO implement update and delete later.
}