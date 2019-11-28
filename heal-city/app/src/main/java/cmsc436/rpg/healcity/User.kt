package cmsc436.rpg.healcity

import android.content.SharedPreferences
import cmsc436.rpg.healcity.ui.map.NearbyPlace
import cmsc436.rpg.healcity.ui.me.Achievement
import java.text.SimpleDateFormat
import java.util.*

object User {

    const val PLAYER_NAME_KEY = "player_name"
    const val PLAYER_LEVEL_KEY = "player_level"
    const val PLAYER_EXP_KEY = "player_exp"
    const val PLAYER_STEPS_KEY = "player_steps"
    const val PLAYER_TARGET_STEPS = "player_target_step"

    fun initPlayer(sharedPref: SharedPreferences, name: String, target: Int) {
        with (sharedPref.edit()) {
            putString(PLAYER_NAME_KEY, name)
            putInt(PLAYER_TARGET_STEPS, target)
            putInt(PLAYER_LEVEL_KEY, 1)
            putInt(PLAYER_EXP_KEY, 0)
            putInt(PLAYER_STEPS_KEY, 0)
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

                if (name != null) {
                    val player = Player(name, target, level, exp, steps)
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
            commit()
        }
    }

    fun addExp(player: Player, reward: Int) {
        with (player) {
            val nextLevel = level * 5
            val exp = exp + reward
            if (exp >= nextLevel) {
                level += 1
            }
        }
    }

    val date: String
        get() =
            SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
                .format(Date()).toString()
}