package cmsc436.rpg.healcity.ui.me

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import cmsc436.rpg.healcity.TutorialFunctions
import android.content.Intent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_me.*

class MeFragment : Fragment() {

    private lateinit var homeViewModel: MeViewModel

    private var achievementList = ArrayList<Achievement>()
    private lateinit var adapter: AchievementsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(MeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_me, container, false)

        var intent = Intent(context!!, TutorialFunctions::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //dummy list
        //for (i in 1..100) achievementList.add(Achievement("Achivement ${i}"))

        adapter = AchievementsAdapter(achievementList)
        achievement_list.layoutManager = LinearLayoutManager(context)
        achievement_list.adapter = adapter

        loadAchievements()
    }

    private fun loadAchievements() {
        var sharedPref = this.activity?.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
        with (sharedPref!!) {
            if (contains(MainActivity.ACHIEVEMENT_KEY)) {
                val achievementJSON = getString(MainActivity.ACHIEVEMENT_KEY, null)
                val achArray = Gson().fromJson(achievementJSON, Array<String>::class.java)
                Toast.makeText(context!!, achArray.toString(), Toast.LENGTH_LONG).show()

                achievement_list.visibility = View.VISIBLE
                no_achievement_warning.visibility = View.GONE
            } else {

            }
        }
    }

    private fun isEquipSaved(sharedPref: SharedPreferences?): Boolean {
        return sharedPref!!.contains(HEAD_KEY) &&
                sharedPref!!.contains(BODY_KEY) &&
                sharedPref!!.contains(HAND_KEY) &&
                sharedPref!!.contains(HAT_KEY) &&
                sharedPref!!.contains(PANTS_KEY) &&
                sharedPref!!.contains(FEET_KEY)
    }

    private fun chooseEquip(equipCard: ImageView?) {
        Log.i(TAG, "equip choose entered")
        Toast.makeText(context, equipCard?.id.toString(), Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "ME_FRAGMENT"
        private const val HEAD_KEY = "HEAD_ITEM"
        private const val BODY_KEY = "BODY_ITEM"
        private const val HAND_KEY = "HAND_ITEM"
        private const val HAT_KEY = "HAT_ITEM"
        private const val PANTS_KEY = "PANTS_ITEM"
        private const val FEET_KEY = "FEET_ITEM"
        private const val temp = 100
    }

}

interface onEquipSelected {
    fun onEquipSelected(meViewModel: MeViewModel)
}
