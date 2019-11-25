package cmsc436.rpg.healcity.ui.missions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cmsc436.rpg.healcity.MainActivity
import cmsc436.rpg.healcity.R
import com.google.android.gms.location.FusedLocationProviderClient
import android.content.SharedPreferences
import android.util.Log
import cmsc436.rpg.healcity.FitnessTrackingActivity


class MissionFragment : Fragment() {

    private lateinit var dashboardViewModel: MissionViewModel
    private lateinit var mListView: ListView
    private lateinit var missionList: ListView
    private lateinit var mListAdapter: ListAdapter
    private lateinit var steps: Number

    var missions = ArrayList<Mission>()

    private val missionTitles = arrayOf("Walking Goal", "Check-In Goal")
    private val missionDesc = arrayOf("Walk 1 mile", "Check-In at 1 location")
    private val missionProg = intArrayOf(0, 0)

    val from = arrayOf("listview_title", "listview_discription", "listview_progress")
    val to = intArrayOf(
        R.id.mission_title,
        R.id.mission_description,
        R.id.mission_progress
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(MissionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_missions, container, false)

        // Set Shared Preferences listener
        val pref =  activity!!.getSharedPreferences(MainActivity.PREF_FILE, Context.MODE_PRIVATE)
        pref.registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                // UPDATE VALS
                Log.i("MISSION FRAGMENT", key)
            })


        val aList = ArrayList<HashMap<String, String>>()

        val button = root.findViewById(R.id.AddNewMissionButton) as Button
        button.setOnClickListener{ view ->
            AddNewMission(view)
        }


        for (i in missionTitles.indices) {
            val hm = HashMap<String, String>()
            hm["listview_title"] = missionTitles[i]
            hm["listview_discription"] = missionDesc[i]
            hm["listview_progress"] = missionProg[i].toString() + "/1"
            aList.add(hm)
        }

        val simpleAdapter = SimpleAdapter(context, aList, R.layout.mission, from, to)

        mListView = root.findViewById(R.id.mission_list)
        mListView.adapter = simpleAdapter


        var intent = Intent(context!!, FitnessTrackingActivity::class.java)
        startActivityForResult(intent, FITNESS_REQ_CODE)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

    }

    fun AddNewMission(v : View) {
        // USE A DIALOG
        Toast.makeText(context!!, "You clicked the button", Toast.LENGTH_SHORT).show()
        val view = layoutInflater.inflate(R.layout.add_mission_dialog, null)
        val dialog = AlertDialog.Builder(context!!)
        dialog.setView(view)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FITNESS_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                steps = data?.getStringExtra(MainActivity.STEP_KEY)!!.toInt()
            }


        }
    }

        companion object {
            private const val LAT_KEY = "USER_LAT"
            private const val LNG_KEY = "USER_LNG"
            private const val TIME_KEY = "USER_TIME"
            private const val FITNESS_REQ_CODE = 1
    }


}