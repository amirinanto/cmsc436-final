package cmsc436.rpg.healcity.ui.missions

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import cmsc436.rpg.healcity.FitnessTrackingActivity


class MissionFragment : Fragment() {

    private lateinit var dashboardViewModel: MissionViewModel
    private lateinit var mListView: ListView
    private lateinit var missionList: ListView
    private lateinit var mListAdapter: ListAdapter
    private lateinit var steps: Number
    private lateinit var mTitles: Array<String>
    private lateinit var mDesc: Array<String>
    private lateinit var mProg: IntArray

    var missions = ArrayList<Mission>()

    private val from = arrayOf("listview_title", "listview_description", "listview_progress")
    private val to = intArrayOf(
        R.id.mission_title,
        R.id.mission_description,
        R.id.mission_progress
    )

    private val aList = ArrayList<HashMap<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(MissionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_missions, container, false)

        mTitles = MainActivity.missionTitles
        mDesc = MainActivity.missionDesc
        mProg = MainActivity.missionProg

        val button = root.findViewById(R.id.AddNewMissionButton) as Button
        button.setOnClickListener{ view ->
            addNewMission(view)
        }

        for (i in mTitles.indices) {
            val hm = HashMap<String, String>()
            hm["listview_title"] = mTitles[i]
            hm["listview_description"] = mDesc[i]
            hm["listview_progress"] = mProg[i].toString() + "/1"
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

    private fun addNewMission(v : View) {
        val view = layoutInflater.inflate(R.layout.add_mission_dialog, null)
        val dialog = AlertDialog.Builder(context!!)
        dialog.setView(view)
        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            // Retrieve values
            val title = view.findViewById<EditText>(R.id.added_mission_title).text.toString()
            val type = view.findViewById<Spinner>(R.id.added_mission_type).selectedItem.toString()
            val length = view.findViewById<Spinner>(R.id.added_mission_length).selectedItem.toString()
            val hm = HashMap<String, String>()

            // Add to list
            hm["listview_title"] = title
            if (type == "Walk") {
                hm["listview_description"] = "Walk $length miles"
            } else {
                hm["listview_description"] = "Check-In at $length locations"
            }
            hm["listview_progress"] = "0/$length"

            //Store values for future
            MainActivity.missionTitles += title
            MainActivity.missionDesc += hm["listview_description"].toString()
            MainActivity.missionProg += 0

            aList.add(hm)

        }
        dialog.setNegativeButton("Cancel") { d: DialogInterface, _: Int ->
            d.cancel()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FITNESS_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                steps = data?.getIntExtra(MainActivity.STEP_KEY, 0)!!
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