package cmsc436.rpg.healcity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro

import android.content.Intent
//import android.support.v4.app.Fragment
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment1
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment2
import cmsc436.rpg.healcity.ui.tutorial.IntroFragment3
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPage



class TutorialFunctions : AppIntro() {

    // need this so second time users don't have to go through the tutorial again
    private var preferenceHelper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
        // var frag = Fragment()

        // comment setContentView
        //setContentView(R.layout.activity_main);

        preferenceHelper = PreferenceHelper(this)

        if (preferenceHelper!!.getIntro().equals("no")) {
            val intent = Intent(this@TutorialFunctions, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }

        addSlide(IntroFragment1()) //extend AppIntro
        addSlide(IntroFragment2())
        addSlide(IntroFragment3())


        //showIntroSlides()
    }

    /*

    private fun showIntroSlides() {
        val pageOne = SliderPage()
            .title(getString(R.string.slide_one_top_text))
            .description(getString(R.string.slide_one_down_text))
            .imageDrawable(R.drawable.logo)
            .bgColor(getColor(R.color.slide_one))
            .build()
        val pageTwo = SliderPage()
            .title(getString(R.string.slide_two_top_text))
            .description(getString(R.string.slide_two_down_text))
            .imageDrawable(R.drawable.notebook_with_logo)
            .bgColor(getColor(R.color.slide_two))
            .build()
        val pageThree = SliderPage()
            .title(getString(R.string.slide_three_top_text))
            .description(getString(R.string.slide_three_down_text))
            .imageDrawable(R.drawable.bow_classic_brown)
            .bgColor(getColor(R.color.slide_three))
            .build()
       /* val pageFour = SliderPage()
            .title(getString(R.string.slide_four_top_text))
            .description(getString(R.string.slide_four_down_text))
            .imageDrawable(R.drawable.taget_and_arrow)
            .bgColor(getColor(R.color.slide_four))
            .build()
        */

        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(IntroFragment2.newInstance(pageTwo))
        addSlide(IntroFragment3.newInstance(pageThree))
        //addSlide(AppIntro2Fragment.newInstance(pageFour))
    }

*/
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        preferenceHelper!!.putIntro("no")

        val intent = Intent(this@TutorialFunctions, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
       // this.finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        preferenceHelper!!.putIntro("no")

        val intent = Intent(this@TutorialFunctions, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        //this.finish()
    }
/*
    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.

    }
*/
}
