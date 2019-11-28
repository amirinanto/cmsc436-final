package cmsc436.rpg.healcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

//create the first or Home activity of our app

//There is one button in this welcome activity.

//When the user clicks this button, compiler will set the value of preference as empty “”.

class WelcomeActivity : AppCompatActivity() {
    private var btn: Button? = null
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        btn = findViewById(R.id.next) as Button
        preferenceHelper = PreferenceHelper(this)

        btn!!.setOnClickListener {
            preferenceHelper!!.putIntro("")
            onBackPressed()
        }
    }
}
