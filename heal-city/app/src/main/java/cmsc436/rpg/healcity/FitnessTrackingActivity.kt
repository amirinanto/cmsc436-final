package cmsc436.rpg.healcity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tracking.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.share
import org.jetbrains.anko.toast
import java.lang.Integer.max
import java.lang.Integer.parseInt


class FitnessTrackingActivity(var isRunning: Boolean = true): AppCompatActivity(), SensorEventListener{

    var doubleBackPressed = false
    var isTrackable = true
    var sensorManager: SensorManager? = null
    private var stepCounter = 0
    private var counterSteps = 0
    private var stepDetector = 0

    // https://medium.com/@ssaurel/create-a-step-counter-fitness-app-for-android-with-kotlin-bbfb6ffe3ea7
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        pause_button.setOnClickListener {
            if (isRunning) { // pause pressed
                stopTracking()
                stop_button.isEnabled = false
                pause_button.text = resources.getString(R.string.resume)

            } else { // resume pressed
                startTracking()
                stop_button.isEnabled = true
                pause_button.text = resources.getString(R.string.pause)
            }
        }

        stop_button.setOnClickListener {
            saveData()
        }

//        Log.d(MainActivity.TAG, "step detector: ${true}" +
//        ", step counter: ${packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)}")
    }

    override fun onResume() {
        super.onResume()
        startTracking()
    }

    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    // https://stackoverflow.com/questions/8430805/clicking-the-back-button-twice-to-exit-an-activity
    override fun onBackPressed() {
        if (doubleBackPressed) {
            saveData()
        }

        doubleBackPressed = true
        Toast.makeText(this, "Press BACK one more to stop tracking", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackPressed = false }, 2000)
    }

    private fun startTracking() {
        isRunning = true
        var stepDetector = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepDetector != null) {
            sensorManager?.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show()
            saveData(1)
        }
    }

    private fun stopTracking() {
        isRunning = false
        sensorManager?.unregisterListener(this)
    }

    private fun saveData(abort: Int = 0){
        val result = Intent()
        if (abort == 1) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            val steps = parseInt(steps_value.text.toString())
            if (steps > 0) {
                result.putExtra(MainActivity.STEP_KEY, steps)
                setResult(Activity.RESULT_OK, result)

                var reward = kotlin.math.max(steps / 100, 1)

                if (!addSteps(steps, reward))
                    toast("Player has not been initialized, please go to welcome screen.")
            }
        }
        finish()
    }

    private fun addSteps(steps: Int, reward: Int): Boolean {
        val sharedPref = applicationContext.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
        if (User.getPlayer(sharedPref) == null)
            return false

        database.use {
            insert(
                DBHelper.TABLE_ACHIEVEMENT,
                DBHelper.COL_NAME to "Done ${steps} steps for ${reward} exp",
                DBHelper.COL_DATE to User.date
            )
        }

        val player = User.getPlayer(sharedPref)!!
        player.steps += steps
        User.addExp(player, reward, sharedPref)
        return true
    }

    override fun onAccuracyChanged(p0: Sensor, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (isRunning) {
            Log.i(MainActivity.TAG, "step detected: ${event.toString()}")

            when (event.sensor.type) {
                Sensor.TYPE_STEP_DETECTOR ->
                    stepCounter++
            }

            steps_value.text = stepCounter.toString()
        }
    }

}