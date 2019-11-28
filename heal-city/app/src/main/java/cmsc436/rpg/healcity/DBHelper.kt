package cmsc436.rpg.healcity

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*

class DBHelper private constructor(context: Context): ManagedSQLiteOpenHelper(context, DB_FILE, null, 1) {

    companion object {
        private var instance: DBHelper? = null

        const val DB_FILE = "heal_city.db"
        const val TABLE_ACHIEVEMENT = "achievements"
        const val TABLE_CIN = "check_in"
        const val COL_NAME = "name"
        const val COL_ID = "placeid"
        const val COL_DATE = "date"

        @Synchronized
        fun getInstance(context: Context) = instance ?: DBHelper(context.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.transaction {
            createTable(
                TABLE_ACHIEVEMENT, true,
                "id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                COL_NAME to TEXT,
                COL_DATE to TEXT
            )
            createTable(
                TABLE_CIN, true,
                COL_ID to TEXT + PRIMARY_KEY + UNIQUE,
                COL_DATE to TEXT
            )
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.transaction {
            dropTable(TABLE_ACHIEVEMENT, true)
            dropTable(TABLE_CIN, true)
        }
        onCreate(db)
    }


}

val Context.database: DBHelper get() = DBHelper.getInstance(this)