package cmsc436.rpg.healcity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.edit


//create the first or Home activity of our app

class WelcomeActivity : AppCompatActivity() {

    private var skipButton: Button? = null
    private var nextButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        skipButton = findViewById(R.id.skip) as Button
        nextButton = findViewById(R.id.next) as Button

        if (!User.isFirstRun(applicationContext))
            finish()

        skipButton!!.setOnClickListener(View.OnClickListener{
            User.firstRunDone(applicationContext)
            finish()
        })

        nextButton!!.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, FirstSlide::class.java))
            finish()
        }

    }

}



