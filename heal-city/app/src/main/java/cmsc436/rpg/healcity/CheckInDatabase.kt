package cmsc436.rpg.healcity

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import java.lang.Exception
import java.net.IDN

class CheckInDatabase(context: Context):
    SQLiteOpenHelper(context, DB_FILE, null, DB_VER) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addCheckIn(placeid: String, date: String) {
        with (ContentValues()) {
            put(COLUMN_ID, placeid)
            put(COLUMN_DATE, date)

            this@CheckInDatabase.writableDatabase.insert(TABLE_NAME, null, this)
        }
    }

    fun isCheckedIn(placeid: String): Boolean {
        var result = false
        var cursor = readableDatabase.rawQuery(
            "SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_ID} = ${placeid} LIMIT 1", null)
        if (cursor.count > 0) {
            result = true
        }
        cursor.close()
        return result
    }

    fun getCheckInOnDate(date: String): ArrayList<String> {
        val checkInList = ArrayList<String>()
        var cursor : Cursor? = null
        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_DATE} LIKE ${date}", null)
        } catch (e: SQLiteException) {
            Log.e(MainActivity.TAG, e.toString())
            writableDatabase.execSQL(SQL_CREATE_ENTRIES)
            return checkInList
        }

        if (cursor!!.moveToFirst())
            while (cursor.isAfterLast == false) {
                with (cursor) {
                    checkInList.add(getString(getColumnIndex(COLUMN_ID)))
                }
            }

        cursor.close()

        return checkInList
    }

    companion object {
        const val DB_FILE = "heal_city.db"
        const val DB_VER = 1

        const val TABLE_NAME = "check_in"
        const val COLUMN_ID = "placeid"
        const val COLUMN_DATE = "date"

        // SQL for creating tables
        const val SQL_CREATE_ENTRIES = "CREATE TABLE ${TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${COLUMN_ID} TEXT" +
                "${COLUMN_DATE} TEXT)"

        // SQL for removing tables
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }
}