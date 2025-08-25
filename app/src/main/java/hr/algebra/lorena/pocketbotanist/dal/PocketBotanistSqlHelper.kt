package hr.algebra.lorena.pocketbotanist.dal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "pocketbotanist.db"
private const val DB_VERSION = 2
const val TABLE_PLANTS = "plants"
const val COLUMN_ID = "_id"
const val COLUMN_NAME = "name"
const val COLUMN_LATIN_NAME = "latinName"
const val COLUMN_DESCRIPTION = "description"
const val COLUMN_WATERING_FREQUENCY = "wateringFrequency"
const val COLUMN_SUNLIGHT_PREFERENCE = "sunlightPreference"
const val COLUMN_IMAGE_URL = "imageUrl"
const val COLUMN_NOTIFICATIONS_ENABLED = "notificationsEnabled"
const val COLUMN_LAST_WATERED_TIMESTAMP = "lastWateredTimestamp"

const val TABLE_NOTIFICATIONS = "notifications"
const val COLUMN_NOTIFICATION_ID = "_id"
const val COLUMN_NOTIFICATION_PLANT_ID = "plant_id"
const val COLUMN_NOTIFICATION_MESSAGE = "message"
const val COLUMN_NOTIFICATION_TIMESTAMP = "timestamp"
const val COLUMN_NOTIFICATION_IS_READ = "is_read"

private const val CREATE_TABLE_PLANTS = "CREATE TABLE $TABLE_PLANTS (" +
        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_NAME TEXT NOT NULL, " +
        "$COLUMN_LATIN_NAME TEXT NOT NULL, " +
        "$COLUMN_DESCRIPTION TEXT NOT NULL, " +
        "$COLUMN_WATERING_FREQUENCY INTEGER NOT NULL, " +
        "$COLUMN_SUNLIGHT_PREFERENCE TEXT NOT NULL, " +
        "$COLUMN_IMAGE_URL TEXT, " +
        "$COLUMN_NOTIFICATIONS_ENABLED INTEGER NOT NULL DEFAULT 1, " +
        "$COLUMN_LAST_WATERED_TIMESTAMP INTEGER NOT NULL DEFAULT 0" +
        ");"

private const val CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE $TABLE_NOTIFICATIONS (" +
        "$COLUMN_NOTIFICATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_NOTIFICATION_PLANT_ID INTEGER NOT NULL, " +
        "$COLUMN_NOTIFICATION_MESSAGE TEXT NOT NULL, " +
        "$COLUMN_NOTIFICATION_TIMESTAMP INTEGER NOT NULL, " +
        "$COLUMN_NOTIFICATION_IS_READ INTEGER NOT NULL DEFAULT 0, " +
        "FOREIGN KEY($COLUMN_NOTIFICATION_PLANT_ID) REFERENCES $TABLE_PLANTS($COLUMN_ID) ON DELETE CASCADE" +
        ");"


class PocketBotanistSqlHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PLANTS)
        db.execSQL(CREATE_TABLE_NOTIFICATIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLANTS")
        onCreate(db)
    }
}