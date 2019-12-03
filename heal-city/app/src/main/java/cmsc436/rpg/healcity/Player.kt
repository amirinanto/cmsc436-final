package cmsc436.rpg.healcity

import android.content.Context
import android.content.SharedPreferences
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import cmsc436.rpg.healcity.ui.me.Achievement
import java.text.SimpleDateFormat
import java.util.*

/**
 * This class is the representation of a Player
 *
 * @author Muchlas Amirinanto
 */
data class Player(val name: String,
                  var target: Int,
                  var level: Int = 1,
                  var exp: Int = 0,
                  var steps: Int = 0,
                  var checkIn: Int = 0)

/**
 * This class will handle the Player's data organization in the
 * sharedPreference
 *
 * @author Muchlas Amirinanto
 */
object User {

    // keys to save values with
    const val PLAYER_NAME_KEY = "player_name"
    const val PLAYER_LEVEL_KEY = "player_level"
    const val PLAYER_EXP_KEY = "player_exp"
    const val PLAYER_STEPS_KEY = "player_steps"
    const val PLAYER_TARGET_STEPS = "player_target_step"
    const val PLAYER_CHECK_IN = "player_check_in"

    // each level is 10 exp
    const val LEVEL_EXP_REQ = 10

    /**
     * This function will create the data for player in sharedPreference
     * with given name and target step
     *
     * @author Muchlas Amirinanto
     */
    fun initPlayer(sharedPref: SharedPreferences, name: String, target: Int) {
        with (sharedPref.edit()) {
            putString(PLAYER_NAME_KEY, name)
            putInt(PLAYER_TARGET_STEPS, target)
            putInt(PLAYER_LEVEL_KEY, 1)
            putInt(PLAYER_EXP_KEY, 0)
            putInt(PLAYER_STEPS_KEY, 0)
            putInt(PLAYER_CHECK_IN, 0)
            commit()
        }
    }

    /**
     * This will return a Player representation based on data stored in sharedPreference
     *
     * @author Muchlas Amirinanto
     */
    fun getPlayer(sharedPref: SharedPreferences): Player? {
        with (sharedPref) {
            if (contains(PLAYER_NAME_KEY)) {
                val name = getString(PLAYER_NAME_KEY, null)
                val target = getInt(PLAYER_TARGET_STEPS, 0)
                val level = getInt(PLAYER_LEVEL_KEY, 0)
                val exp = getInt(PLAYER_EXP_KEY, 0)
                val steps = getInt(PLAYER_STEPS_KEY, 0)
                val checkIn = getInt(PLAYER_CHECK_IN, 0)

                if (name != null) {
                    val player = Player(name, target, level, exp, steps, checkIn)
                    return player
                }
            }
        }
        return null
    }

    /**
     * This will update player's data on sharedPreference
     *
     * @author Muchlas Amirinanto
     */
    fun updatePlayer(sharedPref: SharedPreferences, player: Player) {
        with (sharedPref.edit()) {
            putString(PLAYER_NAME_KEY, player.name)
            putInt(PLAYER_TARGET_STEPS, player.target)
            putInt(PLAYER_LEVEL_KEY, player.level)
            putInt(PLAYER_EXP_KEY, player.exp)
            putInt(PLAYER_STEPS_KEY, player.steps)
            putInt(PLAYER_CHECK_IN, player.checkIn)
            commit()
        }
    }

    /**
     * This will add certain number of exp into the player's statistic and
     * update them in the sharedPreference as necessary
     *
     * @author Muchlas Amirinanto
     */
    fun addExp(player: Player, reward: Int, sharedPref: SharedPreferences) {
        with (player) {
            val nextLevel = level * LEVEL_EXP_REQ
            val exp = exp + reward
            if (exp >= nextLevel) {
                player.level += 1
            }

            player.exp = exp

            updatePlayer(sharedPref, this)
        }
    }

    /**
     * This will return the name and level of current player
     *
     * @author Muchlas Amirinanto
     */
    fun getNameLevel(context: Context): Pair<String, Int> {
        var info = Pair("NO_NAME_SPECIFIED", -1)
        with(context.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)) {
            info = Pair(getString(PLAYER_NAME_KEY, "")!!, getInt(PLAYER_LEVEL_KEY, -1)!!)
        }
        return info
    }

    /**
     * This will calculate the exp required for the next level
     *
     * @author Muchlas Amirinanto
     */
    fun nextLevel(exp: Int): Int
            = exp + (LEVEL_EXP_REQ - exp % LEVEL_EXP_REQ)

    /**
     * This will save data that indicate user has completed the tutorial
     *
     * @author Muchlas Amirinanto
     */
    fun firstRunDone(context: Context) {
        val sharedPref = context.getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt(MainActivity.FIRST_RUN_KEY, 0)
            commit()
        }
    }

    /**
     * This will check if the user has completed the tutorial
     *
     * @author Muchlas Amirinanto
     */

    fun isFirstRun(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE)
        if (sharedPref.contains(MainActivity.FIRST_RUN_KEY))
            return false
        return true
    }

    // this will return today's date in MONTH/DAY/YEAR format as String
    val date: String
        get() =
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                .format(Date()).toString()
}
