package cmsc436.rpg.healcity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment1
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment2
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment3
import com.github.paolorotolo.appintro.AppIntro


//create the first or Home activity of our app

class WelcomeActivity : AppCompatActivity() {

    private var skipButton: Button? = null
    private var nextButton: Button? = null
    private var viewPager: ViewPager? = null
    private var pages: IntArray? = null
    private var prefHelper: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefHelper = PreferenceHelper(this)

        //checking if its the first time the app is launching
        if(!prefHelper!!.checkFirstLaunch()){
            beginHomePage()
            finish()
        }

        //first page
        setContentView(R.layout.activity_welcome)


        skipButton = findViewById(R.id.skip) as Button
        nextButton = findViewById(R.id.next) as Button
        //array of all pages
        pages = intArrayOf(R.layout.fragment_intro_fragment1,R.layout.fragment_intro_fragment2,R.layout.fragment_intro_fragment3)


        skipButton!!.setOnClickListener(View.OnClickListener{
            beginHomePage()
        })

        fun getPage(i:Int):Int{
            return viewPager!!.getCurrentItem() + i
        }

        //go straight to home screen if its the last page of tutorial
        nextButton!!.setOnClickListener(View.OnClickListener(){
            val curr = getPage(+1)
            if (curr < pages!!.size){
                viewPager!!.setCurrentItem(curr)
            } else{
                beginHomePage()
            }
        })
        //addSlide(IntroFragment1())
        //addSlide(IntroFragment2())
        //addSlide(IntroFragment3())

    }

    //skips tutorial and goes straight to main activity
    private fun beginHomePage(){
       prefHelper!!.setFirstLaunch(false)
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

}



