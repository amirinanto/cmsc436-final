package cmsc436.rpg.healcity

import android.content.Context
import android.content.SharedPreferences
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import cmsc436.rpg.healcity.ui.me.Achievement
import java.text.SimpleDateFormat
import java.util.*

data class Player(val name: String,
                  var target: Int,
                  var level: Int = 1,
                  var exp: Int = 0,
                  var steps: Int = 0,
                  var checkIn: Int = 0)

object User {

    const val PLAYER_NAME_KEY = "player_name"
    const val PLAYER_LEVEL_KEY = "player_level"
    const val PLAYER_EXP_KEY = "player_exp"
    const val PLAYER_STEPS_KEY = "player_steps"
    const val PLAYER_TARGET_STEPS = "player_target_step"
    const val PLAYER_CHECK_IN = "player_check_in"

    const val LEVEL_EXP_REQ = 10

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

    fun addExp(player: Player, reward: Int) {
        with (player) {
            val nextLevel = level * LEVEL_EXP_REQ
            val exp = exp + reward
            if (exp >= nextLevel) {
                level += 1
            }
        }
    }

    fun getNameLevel(context: Context): Pair<String, Int> {
        var info = Pair<String, Int>("NO_NAME_SPECIFIED", -1)
        with(context.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)) {
            info = Pair(getString(PLAYER_NAME_KEY, "")!!, getInt(PLAYER_LEVEL_KEY, -1)!!)
        }
        return info
    }

    fun nextLevel(exp: Int): Int
            = exp + (LEVEL_EXP_REQ - exp % LEVEL_EXP_REQ)


    val date: String
        get() =
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                .format(Date()).toString()
}
