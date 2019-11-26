package cmsc436.rpg.healcity.ui.me

import cmsc436.rpg.healcity.TutorialFunctions

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_me.*
import android.content.Intent;


class MeFragment : Fragment() {

    private lateinit var homeViewModel: MeViewModel

    private var achievementList = ArrayList<Achievement>()
    private lateinit var adapter: AchievementsAdapter

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(MeViewModel::class.java)

        var intent = Intent(context!!, TutorialFunctions::class.java)
        startActivity(intent)

        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPref = activity!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

        //dummy list
        //for (i in 1..100) achievementList.add(Achievement("Achivement ${i}"))

        adapter = AchievementsAdapter(achievementList)
        achievement_list.layoutManager = LinearLayoutManager(context)
        achievement_list.adapter = adapter

        loadAchievements()
    }

    private fun loadAchievements() {
        with (sharedPref) {
            if (contains(ACHIEVEMENT_KEY)) {
                val json = getString(ACHIEVEMENT_KEY, null)
                if (json != null)
                    updateAchievementList(Gson().fromJson<ArrayList<Achievement>>(json))
            }
        }
    }

    private fun updateAchievementList(newList: ArrayList<Achievement>) {
        achievementList = newList
        adapter.refresh()
    }

    /**
     * https://stackoverflow.com/questions/33381384/how-to-use-typetoken-generics-with-gson-in-kotlin
     */
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)


    companion object {
        const val ACHIEVEMENT_KEY = "ACH_KEY"
    }

}
