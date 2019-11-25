package cmsc436.rpg.healcity.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cmsc436.rpg.healcity.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [IntroFragment3.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [IntroFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class IntroFragment3 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_fragment3, container, false)
    }

}
