package cmsc436.rpg.healcity.ui.me

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_me.*
import android.content.Intent;
import android.util.Log
import android.widget.Toast
import cmsc436.rpg.healcity.*
import org.jetbrains.anko.db.*


class MeFragment : Fragment() {

    private var achievementList = ArrayList<Achievement>()
    private lateinit var adapter: AchievementsAdapter

    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

//        var intent = Intent(context!!, TutorialFunctions::class.java)
//        startActivity(intent)

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

//        loadDummyAchievements()
        loadAchievements()

        adapter = AchievementsAdapter(achievementList)
        achievement_list.layoutManager = LinearLayoutManager(context)
        achievement_list.adapter = adapter

    }

    private fun loadAchievements() {
        achievementList.clear()
        context!!.database.use {
            select(DBHelper.TABLE_ACHIEVEMENT).orderBy(DBHelper.COL_DEF_ID, SqlOrderDirection.DESC).exec {
                parseList(object : MapRowParser<List<Achievement>> {
                    override fun parseRow(columns: Map<String, Any?>): List<Achievement> {
                        val name = columns.getValue(DBHelper.COL_NAME)
                        val date = columns.getValue(DBHelper.COL_DATE)

                        val achievement = Achievement(name.toString(), date.toString())
                        achievementList.add(achievement)
                        Log.i(MainActivity.TAG, "parsed: ${achievement}")

                        return achievementList
                    }
                })
            }
        }
        if (achievementList.isEmpty()) {
            no_achievement_warning.visibility = View.VISIBLE
        }
    }

    private fun loadDummyAchievements() {
        val dummyList = ArrayList<Achievement>()
        val date = User.date
        for (i in 1..100) {
            dummyList.add(Achievement("achivement .. ${i}", date))
        }
        for (x in dummyList) {
            context!!.database.use {
                insert(DBHelper.TABLE_ACHIEVEMENT,
                    DBHelper.COL_NAME to x.name,
                    DBHelper.COL_DATE to x.date)
            }
        }
    }
}
