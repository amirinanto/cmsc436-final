package cmsc436.rpg.healcity

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // remove title bar and make screen full screen.
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_me, R.id.navigation_mission, R.id.navigation_map
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sharedPref = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

        // Test player
        User.initPlayer(sharedPref, "Muchlas", 1000)



    }



    companion object {
        const val TAG = "HEAL-CITY"
        const val PREF_FILE = "heal_city_pref"

        const val STEP_KEY = "STEP_KEY"

        var missionTitles = arrayOf("Walking Goal", "Check-In Goal")
        var missionDesc = arrayOf("Walk 1 mile", "Check-In at 1 location")
        var missionProg = intArrayOf(0, 0)
    }

}
