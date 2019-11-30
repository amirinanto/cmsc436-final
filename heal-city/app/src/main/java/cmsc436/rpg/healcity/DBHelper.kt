package cmsc436.rpg.healcity

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * This class provides a database access for two tables, check in and achievements.
 * Using a database to store those two information seems to make the most sense for our
 * application.
 *
 * We are using Anko Sqlite library for this database.
 * https://github.com/Kotlin/anko/wiki/Anko-SQLite
 *
 * @author Muchlas Amirinanto
 */
class DBHelper private constructor(context: Context): ManagedSQLiteOpenHelper(context, DB_FILE, null, 1) {

    companion object {
        // name of database
        const val DB_FILE = "heal_city"

        // names of the two tables in database
        const val TABLE_ACHIEVEMENT = "achievements"
        const val TABLE_CIN = "check_in"

        // names of the columns in database
        const val COL_DEF_ID = "_id"
        const val COL_NAME = "name"
        const val COL_ID = "placeid"
        const val COL_DATE = "date"

        // to handle concurrency
        private var instance: DBHelper? = null
        @Synchronized
        fun getInstance(context: Context) = instance ?: DBHelper(context.applicationContext)
    }

    /**
     * We are creating two tables when we initiate the database.
     *
     * @author Muchlas Amirinanto
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.transaction {

            // creating achievement table
            createTable(
                TABLE_ACHIEVEMENT, true,
                COL_DEF_ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                COL_NAME to TEXT,
                COL_DATE to TEXT
            )

            // creating check in table
            createTable(
                TABLE_CIN, true,
                COL_ID to TEXT + PRIMARY_KEY + UNIQUE,
                COL_DATE to TEXT
            )
        }
    }

    /**
     * On upgrade, we are dropping both tables and create new tables.
     *
     * @author Muchlas Amirinanto
     */
    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.transaction {
            dropTable(TABLE_ACHIEVEMENT, true)
            dropTable(TABLE_CIN, true)
        }
        onCreate(db)
    }

}

/**
 * This function extension helps with simplifying database usage.
 *
 * @author Muchlas Amirinanto
 */
val Context.database: DBHelper get() = DBHelper.getInstance(this)