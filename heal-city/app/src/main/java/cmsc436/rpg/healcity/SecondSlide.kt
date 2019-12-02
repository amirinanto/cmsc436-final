package cmsc436.rpg.healcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent

class SecondSlide : AppCompatActivity() {

    private var skipButton: Button? = null
    private var nextButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_slide)

        skipButton = findViewById(R.id.skip) as Button
        nextButton = findViewById(R.id.next) as Button

        skipButton!!.setOnClickListener(View.OnClickListener{
            startActivity(Intent(this@SecondSlide, MainActivity::class.java))
        })

        nextButton!!.setOnClickListener(View.OnClickListener{
            startActivity(Intent(this@SecondSlide, ThirdSlide::class.java))
        })
    }
}
