package hr.algebra.lorena.pocketbotanist.dal

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

private const val AUTHORITY = "hr.algebra.lorena.pocketbotanist.provider"
private const val PATH = "plants"
val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val PLANTS = 10
private const val PLANT_ID = 20

class PocketBotanistProvider : ContentProvider() {

    private lateinit var dbHelper: PocketBotanistSqlHelper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH, PLANTS)
        addURI(AUTHORITY, "$PATH/#", PLANT_ID)
    }

    override fun onCreate(): Boolean {
        dbHelper = PocketBotanistSqlHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            PLANTS -> db.query(TABLE_PLANTS, projection, selection, selectionArgs, null, null, sortOrder)
            PLANT_ID -> {
                val id = uri.lastPathSegment!!
                db.query(TABLE_PLANTS, projection, "$COLUMN_ID = ?", arrayOf(id), null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            PLANTS -> db.insert(TABLE_PLANTS, null, values)
            else -> throw IllegalArgumentException("Unsupported URI for insertion: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper.writableDatabase
        val count = when (uriMatcher.match(uri)) {
            PLANTS -> db.update(TABLE_PLANTS, values, selection, selectionArgs)
            PLANT_ID -> {
                val id = uri.lastPathSegment!!
                db.update(TABLE_PLANTS, values, "$COLUMN_ID = ?", arrayOf(id))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val count = when (uriMatcher.match(uri)) {
            PLANTS -> db.delete(TABLE_PLANTS, selection, selectionArgs)
            PLANT_ID -> {
                val id = uri.lastPathSegment!!
                db.delete(TABLE_PLANTS, "$COLUMN_ID = ?", arrayOf(id))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            PLANTS -> "vnd.android.cursor.dir/$PATH"
            PLANT_ID -> "vnd.android.cursor.item/$PATH"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}