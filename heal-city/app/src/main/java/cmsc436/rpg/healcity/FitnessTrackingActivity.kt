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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_tracking.*

class FitnessTrackingActivity(var isRunning: Boolean = true): AppCompatActivity(), SensorEventListener{

    var doubleBackPressed = false
    var isTrackable = true
    var sensorManager: SensorManager? = null

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
        var stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show()
            isTrackable = false
            pause_button.isEnabled = false
            stop_button.isEnabled = false
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
            result.putExtra(MainActivity.STEP_KEY, steps_value.text.toString().toInt())
            setResult(Activity.RESULT_OK, result)
        }
        finish()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning) {
            steps_value.text = event?.values?.get(0).toString()
        }
    }

}