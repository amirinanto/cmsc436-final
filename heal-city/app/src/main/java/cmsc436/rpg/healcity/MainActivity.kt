package cmsc436.rpg.healcity

import android.Manifest
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import cmsc436.rpg.healcity.ui.map.MapFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences
    lateinit var db: DBHelper

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
        db = DBHelper.getInstance(applicationContext)

        setUpPermission()

    }

    /**
     * Requesting location permission for the app
     * Should only be in the beginning ???
     * TODO
     *
     * @author Muchlas Amirinanto
     */
    private fun setUpPermission() {
        // Here, thisActivity is the current activity
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

            }
        }
    }

    companion object {
        const val TAG = "HEAL-CITY"
        const val PREF_FILE = "heal_city_pref"

        const val STEP_KEY = "steps"
        const val LOCATION_PERMIT = "locationallowed"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        var missionTitles = arrayOf("Walking Goal", "Check-In Goal")
        var missionDesc = arrayOf("Walk 1 mile", "Check-In at 1 location")
        var missionProg = intArrayOf(0, 0)
    }

}
