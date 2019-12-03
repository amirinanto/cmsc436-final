package cmsc436.rpg.healcity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import cmsc436.rpg.healcity.ui.map.MapFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type
import android.content.Intent;
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences
    lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // setting up tabs and fragments
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
        //User.initPlayer(sharedPref, "Muchlas", 1000)

        // initiate database
        db = DBHelper.getInstance(applicationContext)

        // run the tutorial if app has not started before
        if (User.isFirstRun(applicationContext)) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            return
        } else {
            setUpPermission()
        }
    }

    override fun onResume() {
        super.onResume()

        // run the create player activity if user has not set up a character yet
        if (!User.isFirstRun(applicationContext)) {
            if (User.getPlayer(sharedPref) == null)
                startActivityForResult(
                    Intent(
                        applicationContext,
                        CreateCharacterActivity::class.java
                    ),
                    CREATE_CHARACTER_CODE
                )
            // load player informations
            getPlayerInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // user done setting up character
        if (requestCode == CREATE_CHARACTER_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getPlayerInfo()
                setUpPermission()
            }else {
                Toast.makeText(
                    applicationContext,
                    "This application will not work before you setup the character!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    /**
     * This function will retrieve the name and level of character and load them into
     * the top bar informatino
     *
     * @author Muchlas Amirinanto
     */
    fun getPlayerInfo() {
        val (name, level) = User.getNameLevel(applicationContext)
        player_name_top.text = name
        player_level_top.text = level.toString()
    }

    /**
     * Requesting location permission for the app
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

    /**
     * As this is the main activity, we should really ask the user if they want to exit
     *
     * @author Muchlas Amirinanto
     */
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") {
                    _, _ -> finish()}
            .setNegativeButton("No") {
                    _, _ ->  Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()}
            .show()
        return
    }

    companion object {
        const val FIRST_RUN_KEY = "first_run"
        const val TAG = "HEAL-CITY"
        const val PREF_FILE = "heal_city_pref"

        const val STEP_KEY = "steps"

        const val CREATE_CHARACTER_CODE = 0

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        var missionTitles = arrayListOf("Walking Goal", "Check-In Goal")
        var missionDesc = arrayListOf("Walk 1 mile", "Check-In at 1 location")
        var missionProg = arrayListOf(0.0, 0.0)
        var missionLength = arrayListOf(1, 1)
        var checkedInNum = 0
    }

}
