package cmsc436.rpg.healcity.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cmsc436.rpg.healcity.R
import kotlinx.android.synthetic.*
//import android.R




class IntroFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val f = childFragmentManager
        //val pressed = f.findFragmentById(R.id.btn)

        //val pressed = getView().findViewById(R.id.btn)
        // val pressed = getView().findFragmentByTag(R.id.btn)


        return inflater.inflate(R.layout.fragment_intro_fragment1, container, false)
    }

}
