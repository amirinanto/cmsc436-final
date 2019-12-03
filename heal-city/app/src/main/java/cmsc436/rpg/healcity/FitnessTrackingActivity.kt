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

/**
 * This activity will handle the user walk tracking using Android's default sensor
 *
 * @author Muchlas Amirinanto
 */
class FitnessTrackingActivity(var isRunning: Boolean = true): AppCompatActivity(), SensorEventListener{

    // if user really wants to exit
    var doubleBackPressed = false
    var sensorManager: SensorManager? = null
    private var stepCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        // sensor manager to manage the step sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // pause button can either be pause or resume
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
    }

    override fun onResume() {
        super.onResume()
        startTracking()
    }

    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    /**
     * This function override the default back button to make sure user did not exit on accident
     *
     * @author Muchlas Amirinanto
     */
    override fun onBackPressed() {
        if (doubleBackPressed) {
            saveData()
        }

        doubleBackPressed = true
        Toast.makeText(this, "Press BACK one more to stop tracking", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackPressed = false }, 2000)
    }

    /**
     * This function will start user tracking and register step tracking sensor into the listener
     *
     * @author Muchlas Amirinanto
     */
    private fun startTracking() {
        isRunning = true
        var stepDetector = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepDetector != null) {
            sensorManager?.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_UI)
        } else {
            // cancel if no step sensor
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show()
            saveData(1)
        }
    }

    /**
     * This will stop the activity from tracking user's steps
     *
     * @author Muchlas Amirinanto
     */
    private fun stopTracking() {
        isRunning = false
        sensorManager?.unregisterListener(this)
    }

    /**
     * This function will finish the activity and save
     * or discard the result based on the abort parameter
     *
     * @author Muchlas Amirinanto
     */
    private fun saveData(abort: Int = 0){
        val result = Intent()
        if (abort == 1) {
            // discard everything
            setResult(Activity.RESULT_CANCELED)
        } else {
            // save data
            val steps = parseInt(steps_value.text.toString())
            if (steps > 0) {
                result.putExtra(MainActivity.STEP_KEY, steps)
                setResult(Activity.RESULT_OK, result)

                var reward = kotlin.math.max(steps / 100, 1)

                if (!addSteps(steps, reward))
                    toast("Player has not been initialized, please go to welcome screen.")
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
        }
        finish()
    }

    /**
     * This function will record the number of steps into the database
     * and update user's stat as necessary
     *
     * @author Muchlas Amirinanto
     */
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

    // no need to do anything here
    override fun onAccuracyChanged(p0: Sensor, p1: Int) {}

    /**
     * Increment steps when a step is detected
     *
     * @author Muchlas Amirinanto
     */
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