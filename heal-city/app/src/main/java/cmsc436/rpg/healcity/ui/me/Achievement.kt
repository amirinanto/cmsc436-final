package cmsc436.rpg.healcity.ui.me

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.*

data class Achievement(val name: String, val date: String = "-/-/----") {

    fun saveAchievementList(sharedPref: SharedPreferences, array: ArrayList<Achievement>): Boolean {
        val KEY = MeFragment.ACHIEVEMENT_KEY

        with (sharedPref.edit()) {
            var json = Gson().toJson(array)
            putString(KEY, json)
            commit()
        }

        if (sharedPref.contains(KEY))
            return true

        return false
    }
}