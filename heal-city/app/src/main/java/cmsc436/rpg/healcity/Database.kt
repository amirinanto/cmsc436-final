package cmsc436.rpg.healcity

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(
    context: Context?,
    name: String?,
    version: Int,
    openParams: SQLiteDatabase.OpenParams
) : SQLiteOpenHelper(context, name, version, openParams) {
    override fun onCreate(db: SQLiteDatabase?) {
     }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int) {
        }

}