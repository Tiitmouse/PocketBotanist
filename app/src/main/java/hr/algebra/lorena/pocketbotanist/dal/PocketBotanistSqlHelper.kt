package hr.algebra.lorena.pocketbotanist.dal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Database constants
private const val DB_NAME = "pocketbotanist.db"
private const val DB_VERSION = 1
const val TABLE_PLANTS = "plants"
const val COLUMN_ID = "_id"
const val COLUMN_NAME = "name"
const val COLUMN_LATIN_NAME = "latinName"
const val COLUMN_IMAGE_URL = "imageUrl"

// SQL statement to create the plants table
private const val CREATE_TABLE_PLANTS = "CREATE TABLE $TABLE_PLANTS (" +
        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COLUMN_NAME TEXT NOT NULL, " +
        "$COLUMN_LATIN_NAME TEXT NOT NULL, " +
        "$COLUMN_IMAGE_URL TEXT" +
        ");"

class PocketBotanistSqlHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // Called when the database is created for the first time.
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PLANTS)
    }

    // Called when the database needs to be upgraded.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // For simplicity, we'll just drop and recreate the table.
        // In a real app, you would migrate data.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLANTS")
        onCreate(db)
    }
}