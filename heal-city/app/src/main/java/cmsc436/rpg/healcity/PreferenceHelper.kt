package cmsc436.rpg.healcity

import android.content.Context
import android.content.SharedPreferences

// show tutorial just for new users.

class PreferenceHelper(var context: Context) {

    var pref:SharedPreferences
    var editor:SharedPreferences.Editor

    fun checkFirstLaunch():Boolean {
            return pref.getBoolean(FIRST_LAUNCH, true)
    }

    fun setFirstLaunch(FirstLaunch:Boolean) {
            editor.putBoolean(FIRST_LAUNCH, FirstLaunch)
            editor.commit()
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

companion object{
    private val FIRST_LAUNCH = "FirstLaunch"
    private val PREF_NAME = "launch"
    private var PRIVATE_MODE = 0
}
}