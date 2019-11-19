package cmsc436.rpg.healcity.ui.me

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R

class MeFragment : Fragment() {

    private lateinit var homeViewModel: MeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(MeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    // Set up some information about the mQuoteView TextView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var sharedPref = this.activity?.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

    }

    companion object {
        private const val TAG = "ME_FRAGMENT"
    }
}