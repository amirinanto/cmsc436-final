package cmsc436.rpg.healcity

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import cmsc436.rpg.healcity.ui.me.Achievement

/**
 * This class will handle the database side for achievements
 *
 * @author Muchlas Amirinanto
 */
class AchievementDatabase(context: Context):
    SQLiteOpenHelper(context, DB_FILE, null, DB_VER) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVer: Int, newVer: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addAchievement(achievement: Achievement) {
        with (ContentValues()) {
            put(COLUMN_NAME, achievement.name)
            put(COLUMN_DATE, achievement.date)

            this@AchievementDatabase.writableDatabase.insert(TABLE_NAME, null, this)
        }
    }

    fun getAllAchievements(): ArrayList<Achievement> {
        val achievementList = ArrayList<Achievement>()
        var cursor : Cursor? = null
        val db = writableDatabase
        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM ${TABLE_NAME}", null)
        } catch (e: SQLiteException) {
            Log.e(MainActivity.TAG, e.toString())
            db.execSQL(SQL_CREATE_ENTRIES)
            return achievementList
        }

        if (cursor!!.moveToFirst())
            while (cursor.isAfterLast == false) {
                with (cursor) {
                    achievementList.add(
                        Achievement(getString(getColumnIndex(COLUMN_NAME)),
                            getString(getColumnIndex(COLUMN_DATE))))
                }
            }

        cursor.close()

        return achievementList
    }

    companion object {
        const val DB_FILE = "heal_city.db"
        const val DB_VER = 1

        const val TABLE_NAME = "achievements"
        const val COLUMN_NAME = "name"
        const val COLUMN_DATE = "date"

        // SQL for creating tables
        const val SQL_CREATE_ENTRIES = "CREATE TABLE ${TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${COLUMN_NAME} TEXT" +
                "${COLUMN_DATE} TEXT)"

        // SQL for removing tables
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }
}