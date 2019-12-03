package cmsc436.rpg.healcity.ui.me

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_me.*

import android.util.Log
import android.widget.Toast
import cmsc436.rpg.healcity.*
import cmsc436.rpg.healcity.ui.adapters.AchievementsAdapter
import org.jetbrains.anko.db.*


/**
 * This Fragment handles the interaction within the "Me" tab
 *
 * @author Muchlas Amirinanto
 */
class MeFragment : Fragment() {

    // list of achievements
    private var achievementList = ArrayList<Achievement>()

    // adapter for achievement list
    private lateinit var adapter: AchievementsAdapter

    // to access the sharedPreference
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_me, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    override fun onResume() {
        super.onResume()

        // loading player information from sharedPreference into the app
        loadPlayerInfo()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // sharedPreference
        sharedPref = context!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)

        // getting all achivements
        loadAchievements()

        adapter = AchievementsAdapter(achievementList)
        achievement_list.layoutManager = LinearLayoutManager(context!!)
        achievement_list.adapter = adapter

    }

    /**
     * This function will load player information into the views
     *
     * @author Muchlas Amirinanto
     */
    private fun loadPlayerInfo() {

        // get player data
        val player = User.getPlayer(sharedPref)

        if (player != null) {
            val exp = player.exp

            // exp text should be exp/(exp required to next level)
            exp_info.text = "${exp} / ${User.nextLevel(exp)}"

            step_total_info.text = player.steps.toString()

            // get number of checkin from database
            val checkInCount = context!!.database.use {
                query(DBHelper.TABLE_CIN, arrayOf("*"),
                    null, null, null, null, null, null)
                    .count
            }

            // update check in in the database
            player.checkIn = checkInCount
            User.updatePlayer(sharedPref, player)

            // Populating player statistics fields
            target_step.text = player.target.toString()
            places_visited_info.text = checkInCount.toString()
        } else {
            // if player is not initialized (which is impossible), show error
            Toast.makeText(context!!, "Please create player!!", Toast.LENGTH_SHORT).show()
        }

        // refreshes the top top bar information
        (activity as MainActivity).getPlayerInfo()
    }

    /**
     * This function will load achivements from the database into the achivement list
     *
     * @author Muchlas Amirinanto
     */
    private fun loadAchievements() {
        achievementList.clear()

        context!!.database.use {
            select(DBHelper.TABLE_ACHIEVEMENT).orderBy(DBHelper.COL_DEF_ID, SqlOrderDirection.DESC).exec {
                parseList(object : MapRowParser<List<Achievement>> {

                    /**
                     * This function will parse each rows of the result and add them into the list
                     *
                     * @author Muchlas Amirinanto
                     */
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

        // no achivements yet
        if (achievementList.isEmpty()) {
            no_achievement_warning.visibility = View.VISIBLE
        }
    }

    /**
     * This function will populate the list with dummy achivements
     *
     * @author Muchlas Amirinanto
     */
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
